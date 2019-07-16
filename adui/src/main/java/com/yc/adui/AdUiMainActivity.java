package com.yc.adui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yc.adsdk.utils.ToastUtil;
import com.yc.adui.activity.TTSdkShowSplashActivity;
import com.yc.adui.activity.TTSdkSplashActivity;

public class AdUiMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_ui_main);

        ToastUtil.init(getApplicationContext());


//        startActivity(new Intent(AdUiMainActivity.this, TTSdkSplashActivity.class));
        startActivity(new Intent(AdUiMainActivity.this, TTSdkShowSplashActivity.class));


        finish();

    }
}
