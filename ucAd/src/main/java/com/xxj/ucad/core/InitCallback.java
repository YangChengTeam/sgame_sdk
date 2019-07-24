package com.xxj.ucad.core;

public interface InitCallback {
    void onSuccess();

    void onFailure(AdError error);
}
