package com.yc.sgame.core;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

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
    public void init(Context context, Config config) {
        SUcGameSDk.getImpl().init((Activity) context, config);
    }

    @Override
    public void login(Context context, LoginCallback callback) {
        SUcGameSDk.getImpl().login((Activity) context, callback);
    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        SUcGameSDk.getImpl().showSplashAd((Activity) context, null);
    }

    public void showSplashAd(Activity activity, ViewGroup viewGroup) {
        SUcGameSDk.getImpl().showSplashAd(activity, viewGroup);
    }

    public void showBannerAd(Activity activity) {
        SUcGameSDk.getImpl().showBannerAd(activity);
    }

    public void showVideoAd(Activity activity) {
        SUcGameSDk.getImpl().showVideoAd(activity);
    }

    public void showVAd(Activity activity, ViewGroup viewGroup) {
        SUcGameSDk.getImpl().showSplashAd(activity, viewGroup);
    }


    @Override
    public void logout(Context context) {
        SUcGameSDk.getImpl().logout((Activity) context);
    }
}
