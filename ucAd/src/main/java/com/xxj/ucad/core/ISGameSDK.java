package com.xxj.ucad.core;

import android.content.Context;


public interface ISGameSDK {
    void init(Context context, Config config, InitCallback callback);


    void showAd(Context context, AdType type, AdCallback callback);

    void hindAd(AdTypeHind type);

}
