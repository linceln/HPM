package com.olsplus.balancemall.core.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.olsplus.balancemall.core.app.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * bitmap处理工具类
 */
public class BitmapUtil {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;
    }

    public static boolean saveBitmap(Bitmap bitmap, String dir, String name, boolean isShowPhotos) {
        File path = new File(dir);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 其次把文件插入到系统图库
        if (isShowPhotos) {
            try {
                MediaStore.Images.Media.insertImage(MyApplication.getIntstance().getContentResolver(),
                        file.getAbsolutePath(), name, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            MyApplication.getIntstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        }

        return true;
    }

    public static Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyApplication.getApp().getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将图片从本地读到内存时,进行压缩 ,减少内存占用，即图片从File形式变为Bitmap形式
     * 特点: 通过设置采样率, 减少图片的像素, 达到对内存中的Bitmap进行压缩
     *
     * @param srcPath
     * @return
     */
    public static String compressSample(Context context, String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(srcPath, newOpts);

        Bitmap bitmap = null;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float height = 800f;
        float weight = 480f;
        int be = 1;
        if (w > h && w > weight) {
            be = (int) (newOpts.outWidth / weight);
        } else if (w < h && h > height) {
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        try {
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        } catch (Exception e) {
        }
//      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        if (bitmap == null) {
            return null;
        } else {
            return compressQuality(bitmap, FileUtil.cacheFile(context));
        }
    }

    /**
     * 质量压缩
     * 方法说明: 该方法是压缩图片的质量, 注意它不会减少图片的像素,比方说, 你的图片是300K的, 1280*700像素的,
     * 经过该方法压缩后, File形式的图片是在100以下, 以方便上传服务器, 但是你BitmapFactory.decodeFile到内存中,变成Bitmap时,
     * 它的像素仍然是1280*700, 计算图片像素的方法是 bitmap.getWidth()和bitmap.getHeight(),
     * 图片是由像素组成的, 每个像素又包含什么呢? 熟悉PS的人知道, 图片是有色相,明度和饱和度构成的.
     * 该方法的官方文档也解释说, 它会让图片重新构造, 但是有可能图像的位深(即色深)和每个像素的透明度会变化,JPEG onlysupports opaque(不透明),
     * 也就是说以jpeg格式压缩后, 原来图片中透明的元素将消失.所以这种格式很可能造成失真
     * 既然它是改变了图片的显示质量, 达到了对File形式的图片进行压缩, 图片的像素没有改变的话, 那重新读取经过压缩的file为Bitmap时,
     * 它占用的内存并不会少.
     *
     * @param bmp
     * @param file
     * @return
     */
    public static String compressQuality(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;//压缩质量从90开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            quality -= 10;
            if (quality >= 0 && quality <= 100) { //质量必须是0到100
                bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            } else {
                break;
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }
}
