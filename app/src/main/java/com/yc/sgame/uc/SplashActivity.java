package com.yc.sgame.uc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yc.sgame.MainActivity;
import com.yc.sgame.R;
import com.yc.sgame.core.AdCallback;
import com.yc.sgame.core.AdType;
import com.yc.sgame.core.Error;
import com.yc.sgame.core.InitCallback;
import com.yc.sgame.core.SGameSDK;

public class SplashActivity extends BaseActivity {

    private String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
            }
        });
    }

    private void showSplash() {
        SGameSDK.getImpl().showAd(this, AdType.SPLASH, new AdCallback() {
            @Override
            public void onDismissed() {
                if (!SplashActivity.this.isFinishing()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }

            @Override
            public void onNoAd(Error error) {

            }

            @Override
            public void onPresent() {

            }

            @Override
            public void onClick() {

            }
        });
    }
}
