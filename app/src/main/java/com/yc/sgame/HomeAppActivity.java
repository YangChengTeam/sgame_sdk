package com.yc.sgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xxj.ucg.core.Config;
import com.xxj.ucg.core.GameError;
import com.xxj.ucg.core.InitCallback;
import com.xxj.ucg.core.LoginCallback;
import com.xxj.ucg.core.LoginConfigInfo;
import com.xxj.ucg.core.LogoutCallback;
import com.xxj.ucg.core.SGameSDK;
import com.xxj.ucg.uc.utils.ToastUtil;

public class HomeAppActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "GameSdkLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_app);

        initViews();
        initGameSdk();
    }

    private void initViews() {
        Button btnLogin = findViewById(R.id.home_btn_login);
        Button btnLogout = findViewById(R.id.home_btn_logout);

        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }
    private void initGameSdk() {
        ToastUtil.init(getApplicationContext());

        Config config = new Config();
        LoginConfigInfo loginConfigInfo = new LoginConfigInfo();
        loginConfigInfo.setAppId("119474");  //网络游戏
        config.setLoginConfigInfo(loginConfigInfo);
        SGameSDK.getImpl().init(this, config, new InitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "initSGameSDK onSuccess: ");
            }

            @Override
            public void onFailure(GameError error) {
                Log.d(TAG, "initSGameSDK onFailure:  error " + error.getCode() + " -- " + error.getMessage());
                ToastUtil.show("初始化失败", "账户初始化失败");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_btn_login:
                SGameSDK.getImpl().login(HomeAppActivity.this, new LoginCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "main_btn_login onSuccess: login success");
                    }

                    @Override
                    public void onFailure(GameError error) {
                        Log.d(TAG, "main_btn_login onFailure: login failure" + error.getCode() + " -- " + error.getMessage());
                    }
                });
                break;
            case R.id.home_btn_logout:
                SGameSDK.getImpl().logout(HomeAppActivity.this, new LogoutCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "main_btn_login onSuccess: logout success");
                    }

                    @Override
                    public void onFailure(GameError error) {
                        Log.d(TAG, "main_btn_login onFailure: logout failure" + error.getCode() + " -- " + error.getMessage());
                    }
                });
                break;
        }
    }
}
