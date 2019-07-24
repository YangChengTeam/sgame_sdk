package com.xxj.ucg.core;

public interface InitCallback {
    void onSuccess();

    void onFailure(GameError error);
}
