package com.xxj.ucg.core;

public interface LogoutCallback {
    void onSuccess();

    void onFailure(GameError error);
}
