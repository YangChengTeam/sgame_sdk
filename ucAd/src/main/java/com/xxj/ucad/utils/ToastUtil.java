
package com.xxj.ucad.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Description: Toast 显示工具类
 * 注意：此类仅适用于广告SDK Demo效果，CP游戏接入请勿引用！
 * Created by huangjk on 2017/5/31.
 */
public class ToastUtil {

    private static Context sContext;

    public static void init(Context applicationContext) {
        sContext = applicationContext;
    }

    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    public static void show(String tag, final String message) {
        Log.i(tag, message);
        sMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(sContext, message , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
