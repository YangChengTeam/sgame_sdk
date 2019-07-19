package com.yc.adui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yc.adsdk.core.AdCallback;
import com.yc.adsdk.core.AdType;
import com.yc.adsdk.core.Error;
import com.yc.adsdk.core.InitCallback;
import com.yc.adsdk.core.SAdSDK;
import com.yc.adui.R;

public class TTSdkShowSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttsdk_show_splash);

        Log.d("mylog", "onCreate: TTSdkShowSplashActivity");


        FrameLayout flCon= findViewById(R.id.ttsdk_show_splash_fl_con);

        SAdSDK.getImpl().showAd(TTSdkShowSplashActivity.this, AdType.SPLASH, new AdCallback() {
            @Override
            public void onDismissed() {
                goToMainActivity();
            }

            @Override
            public void onNoAd(Error error) {
                goToMainActivity();
            }

            @Override
            public void onPresent() {

            }

            @Override
            public void onClick() {

            }
        },flCon);
    }

    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(TTSdkShowSplashActivity.this, AdSdkMainActivity.class);
        startActivity(intent);
//        mSplashContainer.removeAllViews();
        this.finish();
    }


    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {

            Log.d("mylog", "hasAllPermissionsGranted: grantResults "+grantResults);

            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (hasAllPermissionsGranted(grantResults)) {
//            showAd();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            this.startActivity(intent);
            finish();
        }
    }

}
