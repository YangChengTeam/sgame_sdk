package com.xxj.ucg.core;

import android.content.Context;


public interface ISGameSDK {


    void init(Context context, Config config, InitCallback callback);

    void login(Context context, LoginCallback callback);

    void logout(Context context, LogoutCallback callback);
}
