package com.yc.adsdk.core;

public interface AdCallback {
    void onDismissed();

    void onNoAd(Error error);

    void onPresent();

    void onClick();
}
