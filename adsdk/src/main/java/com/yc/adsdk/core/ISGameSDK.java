package com.yc.adsdk.core;

import android.content.Context;


public interface ISGameSDK {
    void init(Context context, Config config);

    void init(Context context, Config config, InitCallback callback);

    void showAd(Context context, AdType type, AdCallback callback);
}
