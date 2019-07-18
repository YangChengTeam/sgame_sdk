package com.yc.adui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


import com.yc.adsdk.core.AdCallback;
import com.yc.adsdk.core.AdType;
import com.yc.adsdk.core.Error;
import com.yc.adsdk.core.SAdSDK;
import com.yc.adui.R;

public class AdSdkMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "STtAdSDk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_sdk_main);


        Button btnInster = findViewById(R.id.ad_sdk_main_btn_inster);
        Button btnInsterDownload = findViewById(R.id.ad_sdk_main_btn_inster_download);
        Button btnVideoV = findViewById(R.id.ad_sdk_main_btn_video_v);
        Button btnVideoH = findViewById(R.id.ad_sdk_main_btn_video_h);
        Button btnVideoNative = findViewById(R.id.ad_sdk_main_btn_video_native);
        Button btnSplash = findViewById(R.id.ad_sdk_main_btn_splash);
        Button btnBanner = findViewById(R.id.ad_sdk_main_btn_banner);
        Button btnBannerDownload = findViewById(R.id.ad_sdk_main_btn_banner_download);
        Button btnBannerNative = findViewById(R.id.ad_sdk_main_btn_banner_native);

        btnInster.setOnClickListener(this);
        btnInsterDownload.setOnClickListener(this);
        btnVideoV.setOnClickListener(this);
        btnVideoH.setOnClickListener(this);
        btnVideoNative.setOnClickListener(this);
        btnSplash.setOnClickListener(this);
        btnBanner.setOnClickListener(this);
        btnBannerDownload.setOnClickListener(this);
        btnBannerNative.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ad_sdk_main_btn_inster:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.INSTER, new AdCallback() {
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
            case R.id.ad_sdk_main_btn_inster_download:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.INSTER_DOWNLOAD, new AdCallback() {
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
            case R.id.ad_sdk_main_btn_video_h:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.VIDEO, new AdCallback() {
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
            case R.id.ad_sdk_main_btn_video_v:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.VIDEO_V, new AdCallback() {
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
            case R.id.ad_sdk_main_btn_video_native:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.VIDEO_NATIVE, new AdCallback() {
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
            case R.id.ad_sdk_main_btn_splash:
                startActivity(new Intent(AdSdkMainActivity.this, TTSdkShowSplashActivity.class));
                finish();
                break;
            case R.id.ad_sdk_main_btn_banner:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.BANNER, new AdCallback() {
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
            case R.id.ad_sdk_main_btn_banner_download:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.BANNER_DOWNLOAD, new AdCallback() {
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
            case R.id.ad_sdk_main_btn_banner_native:
                SAdSDK.getImpl().showAd(AdSdkMainActivity.this, AdType.BANNER_NATIVE, new AdCallback() {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(AdSdkMainActivity.this, AdSdkMainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
