package com.xxj.uccong.core;

public interface InitCallback {
    void onSuccess();

    void onFailure(GameConError error);
}
