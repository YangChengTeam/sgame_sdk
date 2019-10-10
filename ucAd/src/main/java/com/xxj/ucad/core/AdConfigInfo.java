package com.xxj.ucad.core;

public class AdConfigInfo {
    private String appSecret;
    private String adId;
    private Object ext;

    private String videoPosId;  //视频广告
    private String bannerPosId; //横幅广告
    private String insertPosId; //插屏广告
    private String welcomeId; //开屏广告

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getVideoPosId() {
        return videoPosId;
    }

    public String getBannerPosId() {
        return bannerPosId;
    }

    public String getInsertPosId() {
        return insertPosId;
    }

    public String getWelcomeId() {
        return welcomeId;
    }

    public void setVideoPosId(String videoPosId) {
        this.videoPosId = videoPosId;
    }

    public void setBannerPosId(String bannerPosId) {
        this.bannerPosId = bannerPosId;
    }

    public void setInsertPosId(String insertPosId) {
        this.insertPosId = insertPosId;
    }

    public void setWelcomeId(String welcomeId) {
        this.welcomeId = welcomeId;
    }
}
