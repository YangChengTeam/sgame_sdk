package com.yc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xxj.ucad.core.AdCallback;
import com.xxj.ucad.core.AdConfigInfo;
import com.xxj.ucad.core.AdType;
import com.xxj.ucad.core.Config;
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
        Config config = new Config();
        AdConfigInfo adConfigInfo = new AdConfigInfo();
        adConfigInfo.setAdId("1000007481");
        adConfigInfo.setVideoPosId("1562660861908"); //1475893508926018
        adConfigInfo.setWelcomeId("1498731130656389");
        adConfigInfo.setBannerPosId("1476429649994173");
        adConfigInfo.setInsertPosId("1476429649994172");
        config.setAdConfigInfo(adConfigInfo);

        SAdSDK.getImpl().init(this, config, new InitCallback() {
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
