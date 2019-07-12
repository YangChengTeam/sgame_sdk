package com.yc.sgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.yc.sgame.core.AdCallback;
import com.yc.sgame.core.AdType;
import com.yc.sgame.core.Error;
import com.yc.sgame.core.InitCallback;
import com.yc.sgame.core.LoginCallback;
import com.yc.sgame.core.SGameSDK;
import com.yc.sgame.uc.BaseActivity;
import com.yc.sgame.uc.SplashActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mFlContainer;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        Button btnLogin = findViewById(R.id.main_btn_login);
        Button btnSplash = findViewById(R.id.mainb_btn_splash);
        Button btnBanner = findViewById(R.id.mainb_btn_banner);
        Button btnVideo = findViewById(R.id.mainb_btn_video);
        mFlContainer = findViewById(R.id.main_splash_container);

        btnLogin.setOnClickListener(this);
        btnSplash.setOnClickListener(this);
        btnBanner.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_login:
                SGameSDK.getImpl().login(MainActivity.this, new LoginCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "main_btn_login onSuccess: login success");
                    }

                    @Override
                    public void onFailure(Error error) {
                        Log.d(TAG, "main_btn_login onFailure: login failure" + error.getCode() + " -- " + error.getMessage());
                    }
                });
                break;
            case R.id.mainb_btn_splash:
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
             /*   SGameSDK.getImpl().showAd(MainActivity.this, AdType.SPLASH, new AdCallback() {
                    @Override
                    public void onDismissed() {

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
                });*/
//                SGameSDK.getImpl().showSplashAd(MainActivity.this, mFlContainer);
                break;
            case R.id.mainb_btn_banner:
                SGameSDK.getImpl().showAd(MainActivity.this, AdType.BANNER, new AdCallback() {
                    @Override
                    public void onDismissed() {

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
                break;
            case R.id.mainb_btn_video:
                SGameSDK.getImpl().showAd(MainActivity.this, AdType.VIDEO, new AdCallback() {
                    @Override
                    public void onDismissed() {

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
                break;
        }
    }
}
