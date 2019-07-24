package com.xxj.uccong.core;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.xxj.uccong.uc.SUcConGameSDk;
import com.xxj.uccong.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SGameSDK implements ISGameSDK {
    private static SGameSDK sGameSDK;
    private String TAG = "GameSdkLog";

    public static SGameSDK getImpl() {
        if (sGameSDK == null) {
            synchronized (SGameSDK.class) {
                if (sGameSDK == null) {
                    sGameSDK = new SGameSDK();
                }
            }
        }
        return sGameSDK;
    }

    @Override
    public void init(Context context, Config config, InitCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
//            checkAndPermission(context);
        }
        String ucLoginAppId = null;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appinfo.metaData;
            if (null != bundle) {
                Resources resources = context.getResources();
                ucLoginAppId = resources.getString(bundle.getInt("UC_LOGIN_APPID"));
            }
            Log.d(TAG, "ucSdkInit: ucLoginAppid " + ucLoginAppId);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "AndroidManifest.xml meta->配置问题");
            return;
        }
        if (TextUtils.isEmpty(ucLoginAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在String.xml文件配置游戏appId  11 code:00036");
            return;
        }
        Config config1 = new Config();
        LoginConfigInfo loginConfigInfo = new LoginConfigInfo();
        loginConfigInfo.setAppId(String.valueOf(ucLoginAppId));
        config1.setLoginConfigInfo(loginConfigInfo);
        SUcConGameSDk.getImpl().init(context, config1, callback);
    }

    @Override
    public void login(Context context, LoginCallback callback) {
        SUcConGameSDk.getImpl().login(context, callback);
    }


    @Override
    public void logout(Context context, LogoutCallback callback) {
        SUcConGameSDk.getImpl().logout(context, callback);
    }

    private void checkAndPermission(Context context) {
        List<String> lackedPermission = new ArrayList<String>();
        List<String> necessaryPermissions = getNecessaryPermissions();
        for (String necessaryPermission : necessaryPermissions) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!(context.checkSelfPermission(necessaryPermission) == PackageManager.PERMISSION_GRANTED)) {
                    lackedPermission.add(necessaryPermission);
                }
            }
        }

        // 缺少权限
        if (lackedPermission.size() != 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("警告");
            alertDialog.setMessage("缺少SDK运行必要权限，请开发者确保权限都已经获取再调用此API\n具体动态权限申请请参考附录BaseActivity.java");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        }
    }

    protected List<String> getNecessaryPermissions() {
        return Arrays.asList(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
