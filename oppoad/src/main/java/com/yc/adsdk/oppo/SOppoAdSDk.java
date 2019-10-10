package com.yc.adsdk.oppo;

import android.content.Context;
import android.view.ViewGroup;

import com.yc.adsdk.core.AdCallback;
import com.yc.adsdk.core.AdType;
import com.yc.adsdk.core.Config;
import com.yc.adsdk.core.ISGameSDK;
import com.yc.adsdk.core.InitCallback;

/**
 * Created by caokun on 2019/8/12 19:45.
 */

public class SOppoAdSDk implements ISGameSDK {

    private static SOppoAdSDk sAdSDK;

    public static SOppoAdSDk getImpl() {
        if (sAdSDK == null) {
            synchronized (SOppoAdSDk.class) {
                if (sAdSDK == null) {
                    sAdSDK = new SOppoAdSDk();
                }
            }
        }
        return sAdSDK;
    }

    @Override
    public void init(Context context, Config config) {

    }

    @Override
    public void init(Context context, Config config, InitCallback callback) {

    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {

    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback, ViewGroup viewGroup) {

    }
}
