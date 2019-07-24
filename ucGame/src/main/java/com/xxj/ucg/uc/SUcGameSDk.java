package com.xxj.ucg.uc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xxj.ucg.core.Config;
import com.xxj.ucg.core.GameError;
import com.xxj.ucg.core.ISGameSDK;
import com.xxj.ucg.core.InitCallback;
import com.xxj.ucg.core.LoginCallback;
import com.xxj.ucg.core.LoginConfigInfo;
import com.xxj.ucg.core.LogoutCallback;
import com.xxj.ucg.uc.utils.ToastUtil;

import cn.gundam.sdk.shell.even.SDKEventKey;
import cn.gundam.sdk.shell.even.SDKEventReceiver;
import cn.gundam.sdk.shell.even.Subscribe;
import cn.gundam.sdk.shell.exception.AliLackActivityException;
import cn.gundam.sdk.shell.exception.AliNotInitException;
import cn.gundam.sdk.shell.open.ParamInfo;
import cn.gundam.sdk.shell.open.UCOrientation;
import cn.gundam.sdk.shell.param.SDKParamKey;
import cn.gundam.sdk.shell.param.SDKParams;
import cn.uc.gamesdk.UCGameSdk;

/**
 * Created by mayn on 2019/7/11.
 */

public class SUcGameSDk implements ISGameSDK {

    private final String TAG = "GameSdkLog";


    private boolean isInitGameSuccess = false;
    private boolean isInitAdSuccess = false;

    private Activity mActivity;
    private Handler mHandler;
    //    private Config mConfig;
    private LoginCallback mLoginCallback;


    private static SUcGameSDk sUcGameSDk;
    private InitCallback mSGameInitCallback;
    private LogoutCallback mLogoutCallback;


    public static SUcGameSDk getImpl() {
        if (sUcGameSDk == null) {
            synchronized (SUcGameSDk.class) {
                if (sUcGameSDk == null) {
                    sUcGameSDk = new SUcGameSDk();
                }
            }
        }
        return sUcGameSDk;
    }

    @Override
    public void init(Context context, Config config, InitCallback callback) {
        this.mSGameInitCallback = callback;
        this.mActivity = (Activity) context;
        this.mHandler = new Handler(Looper.getMainLooper());

        LoginConfigInfo loginConfigInfo = config.getLoginConfigInfo();
        String appId = loginConfigInfo.getAppId();
        if (TextUtils.isEmpty(appId)) {
            ToastUtil.show(TAG, "初始化失败，游戏的appId为空  ");
            GameError error = new GameError();
            error.setCode("00035");
            error.setMessage("初始化失败，游戏的appId为空");
            mSGameInitCallback.onFailure(error);
            return;
        }
        initGame(mActivity, appId);
    }

    private void initGame(final Activity activity, final String appId) {
        UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        ucSdkInit(activity, appId);
    }

    @Override
    public void login(Context context, LoginCallback callback) {
        this.mLoginCallback = callback;
        try {
            UCGameSdk.defaultSdk().login((Activity) context, null);
        } catch (AliLackActivityException e) { //Activity对象为null时将抛出此异常。
            e.printStackTrace();
        } catch (AliNotInitException e) { //未初始化或正在初始化时调⽤此接⼝将抛出此异常。
            e.printStackTrace();
        }
    }

    @Override
    public void logout(Context context, LogoutCallback callback) {
        this.mLogoutCallback = callback;
        try {
            UCGameSdk.defaultSdk().logout(mActivity, null);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        } catch (AliNotInitException e) {
            e.printStackTrace();
        }
    }


    private void ucSdkInit(Activity activity, String appId) {
        ParamInfo gameParamInfo = new ParamInfo();
//        gameParamInfo.setGameId(UCSdkConfig.gameId);
        int intAppId = -1;
        try {
            intAppId = Integer.parseInt(appId);
        } catch (Exception e) {
        }
        Log.d(TAG, "ucSdkInit: intAppId " + intAppId);
        gameParamInfo.setGameId(intAppId);//将游戏gameId传⼊ 该参数从开放平台-游戏管理-SDK 接⼊-获取参数那⾥拿
        Log.d(TAG, "ucSdkInit: 66666 intAppId " + intAppId);
        gameParamInfo.setOrientation(UCOrientation.PORTRAIT); //设置屏幕⽅向， PORTRAIT竖屏、LANDSCAPE横 屏，不设置默认为竖屏，横屏游戏 必须设置为LANDSCAPE
        SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);
        sdkParams.put("offline_login", false); //若不需要SDK游客登录模式，可按照下面的方式调用登录用以屏蔽游客模式


        try {
            UCGameSdk.defaultSdk().initSdk(activity, sdkParams); //1.
            Log.d(TAG, "ucSdkInit:  activity " + activity);
            Log.d(TAG, "ucSdkInit:  sdkParams " + sdkParams);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
            Log.d(TAG, "ucSdkInit: error AliLackActivityException " + e.toString());
        }
    }


    SDKEventReceiver receiver = new SDKEventReceiver() {  //所有接收SDK事件的对象必须从SDKEventReceiver派⽣
        @Subscribe(event = SDKEventKey.ON_INIT_SUCC)
        private void onInitSucc() {  //初始化账户成功
            Log.d(TAG, "initSGameSDK initGame: success 3");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    isInitGameSuccess = true;
                    if (isInitAdSuccess && isInitGameSuccess) {
                        mSGameInitCallback.onSuccess();
                    }
                }
            });
        }

        @Subscribe(event = SDKEventKey.ON_INIT_FAILED)
        private void onInitFailed(String data) {  //初始化账户失败
            Log.d(TAG, "initSGameSDK initGame: fail");
            if (mSGameInitCallback != null) {
                GameError error = new GameError();
                error.setMessage(data);
                mSGameInitCallback.onFailure(error);
            }
        }

        @Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
        private void onLoginSucc(String sid) { //登录成功
//            final GameActivity me = mActivity;
//            AccountInfo.instance().setSid(sid);

            Log.d(TAG, "onLoginSucc: sid " + sid);
            if (mLoginCallback != null) {
                mLoginCallback.onSuccess();
            }
        }

        @Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
        private void onLoginFailed(String desc) {  //登录失败
            if (mLoginCallback != null) {
                GameError loginError = new GameError();
                loginError.setMessage(desc);
                loginError.setCode(String.valueOf(GameError.LOGIN_ERROR));
                mLoginCallback.onFailure(loginError);
            }
        }

        @Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
        private void onLogoutSucc() {  //登出成功
            if (mLogoutCallback != null) {
                mLogoutCallback.onSuccess();
            }
        }

        @Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
        private void onLogoutFailed() { //注销失败
            if (mLogoutCallback != null) {
                GameError loginError = new GameError();
                loginError.setCode(String.valueOf(GameError.LOGOUT_ERROR));
                mLogoutCallback.onFailure(loginError);
            }
        }

        @Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
        private void onExit(String desc) {
            // 退出程序
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
//            printMsg(desc);
        }

        @Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
        private void onExitCanceled(String desc) { //取消退出，选择继续游戏
            Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
        }

        @Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
        private void onCreateOrderSucc(String desc) { //支付订单创建成功
            Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
        }

        @Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
        private void onPayUserExit(String desc) { //⽀付界⾯退出
            Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
        }

    };

}
