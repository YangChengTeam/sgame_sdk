package com.yc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yc.sgame.core.AdCallback;
import com.yc.sgame.core.AdType;
import com.yc.sgame.core.Error;
import com.yc.sgame.core.InitCallback;
import com.yc.sgame.core.SGameSDK;
import com.yc.sgame.uc.utils.ToastUtil;

public class GameSdkSplashActivity extends BaseActivity {

//    private String TAG = "GameSdkSplashActivity";
    private String TAG = "GameSdkLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_sdk_splash);
    }

    @Override
    protected void onRequestPermissionSuccess() {
        super.onRequestPermissionSuccess();

        init();
    }

    private void init() {
        Log.d(TAG, "initSGameSDK init: " );
        SGameSDK.getImpl().init(this, null, new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "initSGameSDK onSuccess: " );
                showSplash();
            }

            @Override
            public void onFailure(Error error) {
                Log.d(TAG, "initSGameSDK onFailure:  error" + error.getCode() + " -- " + error.getMessage());
                String code = error.getCode();
                if("6003".equals(code)){ //初始化广告失败
                    ToastUtil.show("初始化失败","广告初始化失败");
                }else if("6004".equals(code)){ //初始化账户失败
                    ToastUtil.show("初始化失败","账户初始化失败");
                }
            }
        });
    }

    private void showSplash() {
        SGameSDK.getImpl().showAd(this, AdType.SPLASH, new AdCallback() {
            @Override
            public void onDismissed() {
                startNext();
            }

            @Override
            public void onNoAd(Error error) {
                startNext();
            }

            @Override
            public void onPresent() {

            }

            @Override
            public void onClick() {

            }
        });
    }

    private void startNext() {
        if (!GameSdkSplashActivity.this.isFinishing()) {
            startActivity(new Intent(GameSdkSplashActivity.this, GameSdkMainActivity.class));
            GameSdkSplashActivity.this.finish();
        }
    }
}
