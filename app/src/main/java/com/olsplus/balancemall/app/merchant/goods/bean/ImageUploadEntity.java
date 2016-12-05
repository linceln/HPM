package com.olsplus.balancemall.app.merchant.goods.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class ImageUploadEntity extends BaseResultEntity {


    /**
     * ret : 0
     * filepath : uploads/product/1/f3c51d970a08d223a9c47e644f5f45125.png
     * token : vWV7POzKlqt3jvFXTrSBMmpY7fMUCRAI1B4caLh8:3IeSV4RRmH4SOWZOftokHCoL7vA=:eyJzY29wZSI6ImhlcGluZ21hbzIiLCJkZWFkbGluZSI6MTQ3ODIyNjYzOX0=
     */

    private String filepath;
    private String token;
    private String compressedPath;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCompressedPath() {
        return compressedPath;
    }

    public void setCompressedPath(String compressedPath) {
        this.compressedPath = compressedPath;
    }

}
