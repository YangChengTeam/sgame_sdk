package com.yc.sgame.core;

public interface InitCallback {
    void onSuccess();

    void onFailure(Error error);
}
