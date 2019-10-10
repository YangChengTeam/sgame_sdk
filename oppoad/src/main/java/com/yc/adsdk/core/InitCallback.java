package com.yc.adsdk.core;

public interface InitCallback {
    void onSuccess();

    void onFailure(Error error);
}
