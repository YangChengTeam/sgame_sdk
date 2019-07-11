package com.yc.sgame.core;

import android.content.Context;

public interface ISGameSDK {
    void init(Context context, Config config);

    void login(Context context, LoginCallback callback);

    void showAd(Context context, AdType type, AdCallback callback);

    void logout(Context context);
}
