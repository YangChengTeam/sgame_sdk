package com.yc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yc.sgame.core.AdCallback;
import com.yc.sgame.core.AdType;
import com.yc.sgame.core.Error;
import com.yc.sgame.core.LoginCallback;
import com.yc.sgame.core.SGameSDK;

public class GameSdkMainActivity extends BaseActivity implements View.OnClickListener {

    //    private String TAG = "GameSdkMainActivity";
    private String TAG = "GameSdkLog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_sdk_main);

        initViews();

    }

    private void initViews() {
        Button btnLogin = findViewById(R.id.main_btn_login);
        Button btnSplash = findViewById(R.id.mainb_btn_splash);
        Button btnBanner = findViewById(R.id.mainb_btn_banner);
        Button btnVideo = findViewById(R.id.mainb_btn_video);
        Button btnInsert = findViewById(R.id.mainb_btn_insert);
        ImageView ivSwitch = findViewById(R.id.main_iv_switch);

        btnLogin.setOnClickListener(this);
        btnSplash.setOnClickListener(this);
        btnBanner.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
        ivSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.main_btn_login) {
            SGameSDK.getImpl().login(GameSdkMainActivity.this, new LoginCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "main_btn_login onSuccess: login success");
                }

                @Override
                public void onFailure(Error error) {
                    Log.d(TAG, "main_btn_login onFailure: login failure" + error.getCode() + " -- " + error.getMessage());
                }
            });
        } else if (i == R.id.mainb_btn_splash) {
            startActivity(new Intent(GameSdkMainActivity.this, GameSdkSplashActivity.class));
        } else if (i == R.id.mainb_btn_banner) {
            SGameSDK.getImpl().showAd(GameSdkMainActivity.this, AdType.BANNER, new AdCallback() {
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
        } else if (i == R.id.mainb_btn_video) {
            SGameSDK.getImpl().showAd(GameSdkMainActivity.this, AdType.VIDEO, new AdCallback() {
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
        } else if (i == R.id.mainb_btn_insert) {
            SGameSDK.getImpl().showAd(GameSdkMainActivity.this, AdType.INSTER, new AdCallback() {
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
        } else if (i == R.id.main_iv_switch) {
            switchOrientation();
        }
    }
}
