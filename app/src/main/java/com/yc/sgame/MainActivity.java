package com.yc.sgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.yc.sgame.core.LoginCallback;
import com.yc.sgame.core.LoginError;
import com.yc.sgame.core.SGameSDK;
import com.yc.sgame.uc.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mFlContainer;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        SGameSDK.getImpl().init(this, null);
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
                    public void onFailure(LoginError error) {
                        Log.d(TAG, "main_btn_login onFailure: login failure" + error.getCode() + " -- " + error.getMessage());
                    }
                });
                break;
            case R.id.mainb_btn_splash:
                SGameSDK.getImpl().showSplashAd(MainActivity.this, mFlContainer);
                break;
            case R.id.mainb_btn_banner:
                SGameSDK.getImpl().showBannerAd(MainActivity.this);
                break;
            case R.id.mainb_btn_video:
                SGameSDK.getImpl().showVideoAd(MainActivity.this);
                break;
        }
    }
}
