package com.olsplus.balancemall.core.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作工具类
 */
public class FileUtil {

    /**
     * 创建图片缓存文件
     *
     * @return
     */
    public static File createPicCacheFile() {

        File file = new File(Environment.getExternalStorageDirectory(), "HePingMao");
        if (!file.exists()) {
            file.mkdirs();
        }
        File imgFile = null;
        try {
            imgFile = new File(file, String.valueOf(System.currentTimeMillis()) + ".jpg");
        } catch (Exception v0) {
            return null;
        }
        return imgFile;
    }

    /**
     * 缓存文件在/sdcard/Android/data/包名/cache中
     *
     * @param context
     * @return
     */
    public static File cacheImageFile(Context context) {

        File file;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            // sdcard
            file = new File(context.getExternalCacheDir(), "compress");
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, String.valueOf(System.currentTimeMillis()) + ".jpg");
        } else {
            // 内存
            file = new File(context.getCacheDir(), "compress");
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, String.valueOf(System.currentTimeMillis()) + ".jpg");
        }
        return file;
    }

    /**
     * 缓存下载的更新包
     *
     * @param context
     * @return
     */
    public static File cacheApkFile(Context context) {

        File file;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            // sdcard
            file = new File(context.getExternalCacheDir(), "upgrade");
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, "HePingMao.apk");
        } else {
            // 内存
            file = new File(context.getCacheDir(), "upgrade");
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, "HePingMao.apk");
        }
        return file;
    }

    /**
     * 在指定的位置创建指定的文件
     *
     * @param filePath 完整的文件路径
     * @param mkdir    是否创建相关的文件夹
     * @throws Exception
     */
    public static void mkFile(String filePath, boolean mkdir) throws Exception {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        file = null;
    }

    /**
     * 在指定的位置创建文件夹
     *
     * @param dirPath 文件夹路径
     * @return 若创建成功，则返回True；反之，则返回False
     */
    public static boolean mkDir(String dirPath) {
        return new File(dirPath).mkdirs();
    }

    /**
     * 删除指定的文件
     *
     * @param filePath 文件路径
     * @return 若删除成功，则返回True；反之，则返回False
     */
    public static boolean delFile(String filePath) {
        return new File(filePath).delete();
    }

    /**
     * 删除指定的文件夹
     *
     * @param dirPath 文件夹路径
     * @param delFile 文件夹中是否包含文件
     * @return 若删除成功，则返回True；反之，则返回False
     */
    public static boolean delDir(String dirPath, boolean delFile) {
        if (delFile) {
            File file = new File(dirPath);
            if (file.isFile()) {
                return file.delete();
            } else if (file.isDirectory()) {
                if (file.listFiles().length == 0) {
                    return file.delete();
                } else {
                    int zfiles = file.listFiles().length;
                    File[] delfile = file.listFiles();
                    for (int i = 0; i < zfiles; i++) {
                        if (delfile[i].isDirectory()) {
                            delDir(delfile[i].getAbsolutePath(), true);
                        }
                        delfile[i].delete();
                    }
                    return file.delete();
                }
            } else {
                return false;
            }
        } else {
            return new File(dirPath).delete();
        }
    }

    /**
     * 压缩文件里的图片
     *
     * @param context
     * @param file
     */
    public static void compressImageFromFile(Context context, File file) {
        int size = 300;
        Bitmap bitmap;
        String path;
        BufferedOutputStream bufferedOutputStream = null;
        if (file != null) {
            try {
                Uri uri = Uri.fromFile(file);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
                int width = options.outWidth;
                int height = options.outHeight;
                double be = 1;
                if (width > size || height > size) {
                    be = (((double) Math.max(width, height))) / 300;
                }
                BitmapFactory.Options options1 = new BitmapFactory.Options();
                options1.inSampleSize = ((int) Math.ceil(be));
                bitmap = BitmapFactory.decodeStream(context.getApplicationContext().getContentResolver()
                        .openInputStream(uri), null, options1);
                path = file.getPath();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(path);
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制文件/文件夹 若要进行文件夹复制，请勿将目标文件夹置于源文件夹中
     *
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若进行文件夹复制，则为True；反之为False
     * @throws Exception
     */
    public static void copy(String source, String target, boolean isFolder)
            throws Exception {
        if (isFolder) {
            (new File(target)).mkdirs();
            File a = new File(source);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (source.endsWith(File.separator)) {
                    temp = new File(source + file[i]);
                } else {
                    temp = new File(source + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(target + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copy(source + "/" + file[i], target + "/" + file[i], true);
                }
            }
        } else {
            int byteread = 0;
            File oldfile = new File(source);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(source);
                File file = new File(target);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fs = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        }
    }

    /**
     * 移动指定的文件（夹）到目标文件（夹）
     *
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若为文件夹，则为True；反之为False
     * @return
     * @throws Exception
     */
    public static boolean move(String source, String target, boolean isFolder)
            throws Exception {
        copy(source, target, isFolder);
        if (isFolder) {
            return delDir(source, true);
        } else {
            return delFile(source);
        }
    }

    /**
     * 获取缓存文件夹
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath;
        //isExternalStorageEmulated()设备的外存是否是用内存模拟的，是则返回true
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageEmulated()) {
            cachePath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    /**
     * 针对原本uri返回的是file:///...来着的，android4.4返回的是content:///...
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}