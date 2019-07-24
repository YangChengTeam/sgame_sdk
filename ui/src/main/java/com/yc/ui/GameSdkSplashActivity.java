package com.yc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xxj.ucad.core.AdCallback;
import com.xxj.ucad.core.AdType;
import com.xxj.ucad.core.InitCallback;
import com.xxj.ucad.core.SAdSDK;
import com.xxj.ucad.core.AdError;
import com.xxj.ucad.utils.ToastUtil;

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

        initAdSdk();
    }

    private void initAdSdk() {
        ToastUtil.init(getApplicationContext());
       /* SGameSDK.getImpl().init(this, null, new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "initSGameSDK onSuccess: ");
//                showSplash();
                startNext();
            }

            @Override
            public void onFailure(com.yc.sgame.core.Error error) {
                Log.d(TAG, "initSGameSDK onFailure:  error " + error.getCode() + " -- " + error.getMessage());
                String code = error.getCode();
                if (String.valueOf(AdError.AD_INIT_ERROR).equals(code)) { //初始化广告失败
                    ToastUtil.show("初始化失败", "广告初始化失败");
                } else if (String.valueOf(AdError.LOGIN_INIT_ERROR).equals(code)) { //初始化账户失败
                    ToastUtil.show("初始化失败", "账户初始化失败");
                }
            }
        });*/

        SAdSDK.getImpl().init(this, null, new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 初始化广告SDK onSuccess");
                showSplash();
            }

            @Override
            public void onFailure(AdError error) {
                String code = error.getCode();
                String message = error.getMessage();
                Throwable throwable = error.getThrowable();
                Log.d(TAG, "onSuccess: 初始化广告SDK onFailure " + "  code " + code + "  message " + message + "  throwable " + throwable);
                startNext();
            }
        });
    }

    private void showSplash() {
        SAdSDK.getImpl().showAd(this, AdType.SPLASH, new AdCallback() {
            @Override
            public void onDismissed() {
                startNext();
            }

            @Override
            public void onNoAd(AdError error) {
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
