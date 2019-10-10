package com.xxj.ucad.core;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.xxj.ucad.ad.SUcAdSdk;
import com.xxj.ucad.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAdSDK implements ISGameSDK {
    private String TAG = "GameSdkLog";

    private static SAdSDK sAdSDK;
    private AdType adType;
    private AdCallback adCallback;

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
    public void init(Context context, Config config, InitCallback callback) {
        ToastUtil.init(context);
        SUcAdSdk.getImpl().init(context, config, callback);
    }


    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            this.adType = type;
            this.adCallback = callback;
            checkAndPermission(context);
        } else {
            SUcAdSdk.getImpl().showAd(context, type, callback);
        }
    }

    @Override
    public void hindAd( AdTypeHind type) {
        SUcAdSdk.getImpl().hindAd(type);
    }



    private void checkAndPermission(Context context) {
        List<String> lackedPermission = new ArrayList<String>();
        List<String> necessaryPermissions = getNecessaryPermissions();
        if (Build.VERSION.SDK_INT >= 23) {
            for (String necessaryPermission : necessaryPermissions) {
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
        } else {
            if (adType != null && adCallback != null) {
                SUcAdSdk.getImpl().showAd(context, adType, adCallback);
            }
        }
    }

    protected List<String> getNecessaryPermissions() {
        return Arrays.asList(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
