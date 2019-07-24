package com.yc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xxj.uccong.core.GameConError;
import com.xxj.uccong.core.InitCallback;
import com.xxj.uccong.core.LoginCallback;
import com.xxj.uccong.core.LogoutCallback;
import com.xxj.uccong.core.SGameSDK;
import com.xxj.uccong.utils.ToastUtil;
import com.xxj.ucad.core.AdCallback;
import com.xxj.ucad.core.AdError;
import com.xxj.ucad.core.AdType;
import com.xxj.ucad.core.SAdSDK;
//import com.yc.sgame.core.Error;
//import com.yc.sgame.core.InitCallback;
//import com.yc.sgame.core.LoginCallback;
//import com.yc.sgame.core.LogoutCallback;
//import com.yc.sgame.uc.utils.ToastUtil;

public class GameSdkMainActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "GameSdkLog";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_sdk_main);

        initViews();

        initGameSdk();
    }

    private void initGameSdk() {
        ToastUtil.init(getApplicationContext());
        /*SGameSDK.getImpl().init(this, null, new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "initSGameSDK onSuccess: ");
            }

            @Override
            public void onFailure(Error error) {
                Log.d(TAG, "initSGameSDK onFailure:  error " + error.getCode() + " -- " + error.getMessage());
                ToastUtil.show("初始化失败", "账户初始化失败");
            }
        });*/

        SGameSDK.getImpl().init(this, null, new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "initSGameSDK onSuccess: ");
            }

            @Override
            public void onFailure(GameConError error) {
                Log.d(TAG, "initSGameSDK onFailure:  error " + error.getCode() + " -- " + error.getMessage());
                ToastUtil.show("初始化失败", "账户初始化失败");
            }
        });
    }

    private void initViews() {
        Button btnLogin = findViewById(R.id.main_btn_login);
        Button btnLogout = findViewById(R.id.main_btn_logout);
        Button btnSplash = findViewById(R.id.mainb_btn_splash);
        Button btnBanner = findViewById(R.id.mainb_btn_banner);
        Button btnVideo = findViewById(R.id.mainb_btn_video);
        Button btnInsert = findViewById(R.id.mainb_btn_insert);
        ImageView ivSwitch = findViewById(R.id.main_iv_switch);

        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
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
                public void onFailure(GameConError error) {
                    Log.d(TAG, "main_btn_login onFailure: login failure" + error.getCode() + " -- " + error.getMessage());
                }
            });
        } else if (i == R.id.main_btn_logout) {
            SGameSDK.getImpl().logout(GameSdkMainActivity.this, new LogoutCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "main_btn_login onSuccess: logout success");
                }

                @Override
                public void onFailure(GameConError error) {
                    Log.d(TAG, "main_btn_login onFailure: logout failure" + error.getCode() + " -- " + error.getMessage());
                }
            });
        } else if (i == R.id.mainb_btn_splash) {
            startActivity(new Intent(GameSdkMainActivity.this, GameSdkSplashActivity.class));
        } else if (i == R.id.mainb_btn_banner) {
            SAdSDK.getImpl().showAd(GameSdkMainActivity.this, AdType.BANNER, new AdCallback() {
                @Override
                public void onDismissed() {

                }

                @Override
                public void onNoAd(AdError error) {

                }

                @Override
                public void onPresent() {

                }

                @Override
                public void onClick() {

                }
            });
        } else if (i == R.id.mainb_btn_video) {
            SAdSDK.getImpl().showAd(GameSdkMainActivity.this, AdType.VIDEO, new AdCallback() {
                @Override
                public void onDismissed() {

                }

                @Override
                public void onNoAd(AdError error) {

                }

                @Override
                public void onPresent() {

                }

                @Override
                public void onClick() {

                }
            });
        } else if (i == R.id.mainb_btn_insert) {
            SAdSDK.getImpl().showAd(GameSdkMainActivity.this, AdType.INSTER, new AdCallback() {
                @Override
                public void onDismissed() {

                }

                @Override
                public void onNoAd(AdError error) {

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
