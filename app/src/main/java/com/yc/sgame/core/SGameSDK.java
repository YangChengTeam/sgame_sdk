package com.yc.sgame.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yc.sgame.uc.SUcGameSDk;
import com.yc.sgame.uc.utils.ToastUtil;

public class SGameSDK implements ISGameSDK {
    private static SGameSDK sGameSDK;
    private String TAG = "GameSdkLog";

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
        String ucLoginAppId = null, ucAdAppId = null, ucAdVideoPosId = null, ucAdBannerPosId = null, ucAdWelcomePosId = null, ucAdInsertPosId = null;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appinfo.metaData;
            if (null != bundle) {
                Resources resources = context.getResources();
                ucLoginAppId = resources.getString(bundle.getInt("UC_LOGIN_APPID"));
                ucAdAppId = resources.getString(bundle.getInt("UC_AD_APPID"));
                ucAdVideoPosId = resources.getString(bundle.getInt("UC_AD_VIDEO_POSID"));
                ucAdBannerPosId = resources.getString(bundle.getInt("UC_AD_BANNER_POSID"));
                ucAdWelcomePosId = resources.getString(bundle.getInt("UC_AD_WELCOME_POSID"));
                ucAdInsertPosId = resources.getString(bundle.getInt("UC_AD_INSERT_POSID"));
            }
            Log.d(TAG, "ucSdkInit: ucLoginAppid " + ucLoginAppId + " ucAdAppId " + ucAdAppId +
                    " ucAdVideoPosId " + ucAdVideoPosId + " ucAdBannerPosId " + ucAdBannerPosId + " ucAdWelcomePosId " + ucAdWelcomePosId + " ucAdInsertPosId " + ucAdInsertPosId);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "AndroidManifest.xml meta->配置问题");
            return;
        }
        if (TextUtils.isEmpty(ucLoginAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在String.xml文件配置游戏appId   code:00036");
            return;
        }

        if (TextUtils.isEmpty(ucAdAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在String.xml文件配置广告appId   code:00046");
            return;
        }

        Config config1 = new Config();
        LoginConfigInfo loginConfigInfo = new LoginConfigInfo();
        loginConfigInfo.setAppId(String.valueOf(ucLoginAppId));
        AdConfigInfo adConfigInfo = new AdConfigInfo();
        adConfigInfo.setAppId(String.valueOf(ucAdAppId));
        adConfigInfo.setVideoPosId(ucAdVideoPosId);
        adConfigInfo.setBannerPosId(ucAdBannerPosId);
        adConfigInfo.setBannerPosId(ucAdBannerPosId);
        adConfigInfo.setWelcomeId(ucAdWelcomePosId);
        adConfigInfo.setInsertPosId(ucAdInsertPosId);
        config1.setLoginConfigInfo(loginConfigInfo);
        config1.setAdConfigInfo(adConfigInfo);
        SUcGameSDk.getImpl().init(context, config1, callback);
    }

    @Override
    public void login(Context context, LoginCallback callback) {
        SUcGameSDk.getImpl().login(context, callback);
    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        SUcGameSDk.getImpl().showAd(context, type, callback);
    }


    @Override
    public void logout(Context context, LogoutCallback callback) {
        SUcGameSDk.getImpl().logout(context, callback);
    }
}
