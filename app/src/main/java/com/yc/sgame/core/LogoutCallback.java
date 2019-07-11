package com.yc.sgame.core;

public interface LogoutCallback {
    void onSuccess();

    void onFailure(Error error);
}
