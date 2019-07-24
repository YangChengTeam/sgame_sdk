package com.xxj.ucg.core;

public interface LoginCallback {

    void onSuccess();

    void onFailure(GameError error);

}
