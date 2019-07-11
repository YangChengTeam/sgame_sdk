package com.yc.sgame.core;

import android.content.Context;

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
    public void init(Context context, Config config) {

    }

    @Override
    public void login(Context context, LoginCallback callback) {

    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {

    }

    @Override
    public void logout(Context context) {

    }
}
