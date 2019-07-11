package com.yc.sgame.core;

public interface LoginCallback {

    void onSuccess();

    void onFailure(LoginError error);

}
