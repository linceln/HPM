package com.olsplus.balancemall.core.util;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncrypUtil {

    private static byte[] eccrypt(String info, String shaType) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(shaType);
        byte[] srcBytes = info.getBytes();
        // 使用srcBytes更新摘要
        sha.update(srcBytes);
        // 完成哈希计算，得到result
        byte[] resultBytes = sha.digest();
        return resultBytes;
    }

    @NonNull
    public static String hexString(byte[] bytes) {
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int val = ((int) bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    @Nullable
    public static String eccryptSHA1(String info) {
        try {
            byte[] data = eccrypt(info, "SHA1");
            if(data == null){
                return null;
            }
            return hexString(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
