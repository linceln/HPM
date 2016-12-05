package com.olsplus.balancemall.core.util;


import com.qiniu.android.storage.UploadManager;

public class QiNiuUtil {

    private static class UploadManagerHolder {
        private static final UploadManager instance = new UploadManager();
    }

    public static UploadManager getInstance() {
        return UploadManagerHolder.instance;
    }
}
