package com.yc.sgame.uc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yc.sgame.core.AdCallback;
import com.yc.sgame.core.AdError;
import com.yc.sgame.core.AdType;
import com.yc.sgame.core.Config;
import com.yc.sgame.core.ISGameSDK;
import com.yc.sgame.core.LoginCallback;
import com.yc.sgame.core.LoginError;
import com.yc.sgame.uc.config.AdConfig;
import com.yc.sgame.uc.config.UCSdkConfig;
import com.yc.sgame.uc.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import cn.gundam.sdk.shell.even.SDKEventKey;
import cn.gundam.sdk.shell.even.SDKEventReceiver;
import cn.gundam.sdk.shell.even.Subscribe;
import cn.gundam.sdk.shell.exception.AliLackActivityException;
import cn.gundam.sdk.shell.exception.AliNotInitException;
import cn.gundam.sdk.shell.open.ParamInfo;
import cn.gundam.sdk.shell.open.UCOrientation;
import cn.gundam.sdk.shell.param.SDKParamKey;
import cn.gundam.sdk.shell.param.SDKParams;
import cn.sirius.nga.NGASDK;
import cn.sirius.nga.NGASDKFactory;
import cn.sirius.nga.properties.NGABannerController;
import cn.sirius.nga.properties.NGABannerListener;
import cn.sirius.nga.properties.NGABannerProperties;
import cn.sirius.nga.properties.NGAVideoController;
import cn.sirius.nga.properties.NGAVideoListener;
import cn.sirius.nga.properties.NGAVideoProperties;
import cn.sirius.nga.properties.NGAWelcomeListener;
import cn.sirius.nga.properties.NGAWelcomeProperties;
import cn.sirius.nga.properties.NGAdController;
import cn.uc.gamesdk.UCGameSdk;

import static cn.sirius.nga.NGASDKFactory.getNGASDK;

/**
 * Created by mayn on 2019/7/11.
 */

public class SUcGameSDk implements ISGameSDK {

    private final String TAG = "SUcGameSDk";

    private Activity mActivity;
    private Handler mHandler;
    private Config mConfig;
    private LoginCallback mLoginCallback;
    private AdCallback mAdCallback;
    private NGAVideoController mNGAVideoController;


    private static SUcGameSDk sUcGameSDk;

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
    public void init(Context context, Config config) {
        this.mConfig = config;
        this.mActivity = (Activity) context;
        this.mHandler = new Handler(Looper.getMainLooper());


        initGame(mActivity);
        initAd(mActivity);
    }

    @Override
    public void login(Context context, LoginCallback callback) {
        this.mLoginCallback = callback;
        try {
            UCGameSdk.defaultSdk().login(mActivity, null);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        } catch (AliNotInitException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        this.mAdCallback = callback;
        switch (type) {
            case BANNER:
                showBannerAd(mActivity);
                break;
            case SPLASH:
                showSplashAd(mActivity, null);
                break;
            case VIDEO:
                showVideoAd(mActivity);
                break;
        }
    }

    public void showBannerAd(Activity activity) {
        loadBannerAd(activity);
    }

    private RelativeLayout mBannerView;
    private ViewManager mWindowManager;

    //为了提高广告的填充率以及曝光量，建议重新加载广告时候重新调用此方法，重新请求新的广告内容
    private void loadBannerAd(Activity activity) {
        if (mBannerView != null && mBannerView.getParent() != null) {
            mWindowManager.removeView(mBannerView);
        }
        mBannerView = new RelativeLayout(activity);

        //此代码是为了显示广告区域，游戏请根据游戏主题背景决定是否要添加
        mBannerView.setBackgroundColor(activity.getResources().getColor(android.R.color.darker_gray));

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManager = (WindowManager) activity.getSystemService(activity.WINDOW_SERVICE);
        mWindowManager.addView(mBannerView, params);

        NGABannerProperties properties = new NGABannerProperties(activity, AdConfig.appId, AdConfig.bannerPosId, mBannerView);
        properties.setListener(mBannerAdListener);
        NGASDK ngasdk = NGASDKFactory.getNGASDK();
        ngasdk.loadAd(properties);

        // 若需要默认横幅广告不展示
//        mBannerView.setVisibility(View.GONE);
    }
    //注意：请在Activity成员变量保存，使用匿名内部类可能导致回收
    NGABannerListener mBannerAdListener = new NGABannerListener() {
        @Override
        public void onRequestAd() {
//            ToastUtil.show(TAG, "onRequestAd");
        }

        @Override
        public <T extends NGAdController> void onReadyAd(T controller) {
            NGABannerController ngaBannerController = (NGABannerController) controller;
//            ToastUtil.show(TAG, "onReadyAd");
//            showAd(BannerActivity.this);
            if (ngaBannerController != null) {
                ngaBannerController.showAd();
//                mBannerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onShowAd() {
//            ToastUtil.show(TAG, "onShowAd");
        }

        @Override
        public void onCloseAd() {
            //广告关闭之后mController置null，鼓励加载广告重新调用loadAd，提高广告填充率
            mNGAVideoController = null;
//            mBannerView.setVisibility(View.GONE);
//            ToastUtil.show(TAG, "onCloseAd");
        }

        @Override
        public void onErrorAd(int code, String message) {
            ToastUtil.show(TAG, "onErrorAd errorCode:" + code + ", message:" + message);
        }

        @Override
        public void onClickAd() {
//            ToastUtil.show(TAG, "onClickAd");
        }
    };




    @Override
    public void logout(Context context) {
        try {
            UCGameSdk.defaultSdk().logout(mActivity, null);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        } catch (AliNotInitException e) {
            e.printStackTrace();
        }
    }


    public void showVideoAd(Activity activity) {
        loadVideoAd(activity);
    }

    private void loadVideoAd(Activity activity) {
        NGAVideoProperties properties = new NGAVideoProperties(activity, AdConfig.appId, AdConfig.videoPosId);
        properties.setListener(mVideoAdListener);
        NGASDK ngasdk = NGASDKFactory.getNGASDK();
        ngasdk.loadAd(properties);
    }

    public void showSplashAd(Activity activity, ViewGroup container) {
        NGAWelcomeProperties properties = new NGAWelcomeProperties(activity, AdConfig.appId, AdConfig.welcomeId, container);
        // 支持开发者自定义的跳过按钮。SDK要求skipContainer一定在传入后要处于VISIBLE状态，且其宽高都不得小于3x3dp。
        // 如果需要使用SDK默认的跳过按钮，可以选择上面两个构造方法。
        //properties.setSkipView(skipView);
        properties.setListener(mSplashAdListener);
        NGASDK ngasdk = getNGASDK();
        ngasdk.loadAd(properties);
    }

    NGAVideoListener mVideoAdListener = new NGAVideoListener() {

        @Override
        public void onShowAd() {
            ToastUtil.show(TAG, "onShowAd ");
            if (mAdCallback != null) {
                mAdCallback.onPresent();
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(TAG, "mNGAVideoController " + mNGAVideoController);
                    if (mNGAVideoController != null) {
                        mNGAVideoController.showAd();
                    }
                }
            });
        }

        @Override
        public void onClickAd() {
            if (mAdCallback != null) {
                mAdCallback.onClick();
            }
        }

        @Override
        public void onCloseAd() {
            if (mAdCallback != null) {
                mAdCallback.onDismissed();
            }
        }

        @Override
        public void onErrorAd(final int code, final String message) {
            ToastUtil.show(TAG, "onErrorAd " + " code " + code + "  message " + message);
            if (mAdCallback != null) {
                AdError adError = new AdError();
                adError.setCode(String.valueOf(code));
                adError.setMessage(message);
                mAdCallback.onNoAd(adError);
            }
        }

        @Override
        public void onRequestAd() {
        }

        @Override
        public <T extends NGAdController> void onReadyAd(T controller) {

            mNGAVideoController = (NGAVideoController) controller;
            if (mNGAVideoController != null) {  //播放视频
                mNGAVideoController.showAd();
            }
        }

        @Override
        public void onCompletedAd() {
            ToastUtil.show(TAG, "onCompletedAd");
        }
    };

    //注意：请在Activity成员变量保存，使用匿名内部类可能导致回收
    private NGAWelcomeListener mSplashAdListener = new NGAWelcomeListener() {

        @Override
        public void onClickAd() {
            if (mAdCallback != null) {
                mAdCallback.onClick();
            }
        }

        @Override
        public void onErrorAd(int code, String message) {
            if (mAdCallback != null) {
                AdError adError = new AdError();
                adError.setCode(String.valueOf(code));
                adError.setMessage(message);
                mAdCallback.onNoAd(adError);
            }
        }

        @Override
        public void onShowAd() {
            // 广告展示后一定要把预设的开屏图片隐藏起来
//            splashHolder.setVisibility(View.INVISIBLE);
//            ToastUtil.show(TAG, "onShowAd");
            if (mAdCallback != null) {
                mAdCallback.onPresent();
            }

        }

        @Override
        public void onCloseAd() {
            //无论成功展示成功或失败都回调用该接口，所以开屏结束后的操作在该回调中实现
            if (mAdCallback != null) {
                mAdCallback.onDismissed();
            }
        }

        @Override
        public <T extends NGAdController> void onReadyAd(T controller) {
            // 开屏广告是闪屏过程自动显示不需要NGAdController对象，所以返回controller为null；
//            ToastUtil.show(TAG, "onReadyAd");
        }

        @Override
        public void onRequestAd() {
//            ToastUtil.show(TAG, "onRequestAd");
        }


        /**
         * 倒计时回调，返回广告还将被展示的剩余时间。
         * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
         *
         * @param millisUntilFinished 剩余毫秒数
         */
        @Override
        public void onTimeTickAd(long millisUntilFinished) {

        }
    };


    private void initGame(Activity activity) {
        ucSdkInit(activity);
        UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
    }

    private void initAd(Activity activity) {
        NGASDK ngasdk = getNGASDK();
        Map<String, Object> args = new HashMap<>();
        args.put(NGASDK.APP_ID, AdConfig.appId);
        ngasdk.init(activity, args, mInitCallback);
    }

    private NGASDK.InitCallback mInitCallback = new NGASDK.InitCallback() {
        @Override
        public void success() {

        }

        @Override
        public void fail(Throwable throwable) {

        }
    };

    private void ucSdkInit(Activity activity) {
        ParamInfo gameParamInfo = new ParamInfo();

        gameParamInfo.setGameId(UCSdkConfig.gameId);

        gameParamInfo.setOrientation(UCOrientation.PORTRAIT);

        SDKParams sdkParams = new SDKParams();
        sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);


        try {
            UCGameSdk.defaultSdk().initSdk(activity, sdkParams);
        } catch (AliLackActivityException e) {
            e.printStackTrace();
        }
    }


    SDKEventReceiver receiver = new SDKEventReceiver() {
        @Subscribe(event = SDKEventKey.ON_INIT_SUCC)
        private void onInitSucc() {
            //初始化成功
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                }
            });
        }

        @Subscribe(event = SDKEventKey.ON_INIT_FAILED)
        private void onInitFailed(String data) {  //初始化失败

        }

        @Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
        private void onLoginSucc(String sid) { //登录成功
//            final GameActivity me = mActivity;
//            AccountInfo.instance().setSid(sid);
            mLoginCallback.onSuccess();
        }

        @Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
        private void onLoginFailed(String desc) {  //登录失败
            LoginError loginError = new LoginError();
            loginError.setMessage(desc);
            mLoginCallback.onFailure(loginError);
        }

        @Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
        private void onLogoutSucc() {  //登出成功

        }

        @Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
        private void onLogoutFailed() { //注销失败
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
        private void onExitCanceled(String desc) {
            Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
        }

    };
}