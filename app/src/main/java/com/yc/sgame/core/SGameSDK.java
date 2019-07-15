package com.yc.sgame.core;

import android.app.Activity;
import android.content.Context;

import com.yc.sgame.uc.SUcGameSDk;

public class SGameSDK implements ISGameSDK {
    private static SGameSDK sGameSDK;

    public static SGameSDK getImpl() {
        if (sGameSDK == null) {
            synchronized (SGameSDK.class) {
                if (sGameSDK == null) {
                    sGameSDK = new SGameSDK();
                }
            }
        }
        return sGameSDK;
    }

    @Override
    public void init(Context context, Config config, InitCallback callback) {
        SUcGameSDk.getImpl().init(context, config, callback);
    }

    @Override
    public void login(Context context, LoginCallback callback) {
        SUcGameSDk.getImpl().login(context, callback);
    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        SUcGameSDk.getImpl().showAd(context, type, callback);
    }

    public void showInsertAd(Context context) {
        SUcGameSDk.getImpl().loadInsertAd((Activity) context);
    }

    @Override
    public void logout(Context context, LogoutCallback callback) {
        SUcGameSDk.getImpl().logout(context, callback);
    }
}
