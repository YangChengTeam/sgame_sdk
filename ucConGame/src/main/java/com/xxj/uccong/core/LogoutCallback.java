package com.xxj.uccong.core;

public interface LogoutCallback {
    void onSuccess();

    void onFailure(GameConError error);
}
