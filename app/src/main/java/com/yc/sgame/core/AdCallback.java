package com.yc.sgame.core;

public interface AdCallback {
    void onDismissed();

    void onNoAd(AdError error);

    void onPresent();

    void onClick();
}
