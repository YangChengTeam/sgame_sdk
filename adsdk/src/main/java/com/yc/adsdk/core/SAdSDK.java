package com.yc.adsdk.core;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.yc.adsdk.tt.STtAdSDk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAdSDK implements ISGameSDK {
    private static SAdSDK sAdSDK;
    private String TAG = "GameSdkLog";

    public static SAdSDK getImpl() {
        if (sAdSDK == null) {
            synchronized (SAdSDK.class) {
                if (sAdSDK == null) {
                    sAdSDK = new SAdSDK();
                }
            }
        }
        return sAdSDK;
    }

    @Override
    public void init(Context context, Config config) {
        init(context, config, null);
    }

    @Override
    public void init(Context context, Config config, InitCallback callback) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            checkAndPermission(context);
//        }

        STtAdSDk.getImpl().init(context, config, callback);
    }


    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        STtAdSDk.getImpl().showAd(context, type, callback);
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
