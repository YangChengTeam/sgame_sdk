package com.xxj.ucad.ad;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.xxj.ucad.core.AdCallback;
import com.xxj.ucad.core.AdConfigInfo;
import com.xxj.ucad.core.AdType;
import com.xxj.ucad.core.Config;
import com.xxj.ucad.core.AdError;
import com.xxj.ucad.core.ISGameSDK;
import com.xxj.ucad.core.InitCallback;
import com.xxj.ucad.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import cn.sirius.nga.NGASDK;
import cn.sirius.nga.NGASDKFactory;
import cn.sirius.nga.properties.NGABannerController;
import cn.sirius.nga.properties.NGABannerListener;
import cn.sirius.nga.properties.NGABannerProperties;
import cn.sirius.nga.properties.NGAInsertController;
import cn.sirius.nga.properties.NGAInsertListener;
import cn.sirius.nga.properties.NGAInsertProperties;
import cn.sirius.nga.properties.NGAVideoController;
import cn.sirius.nga.properties.NGAVideoListener;
import cn.sirius.nga.properties.NGAVideoProperties;
import cn.sirius.nga.properties.NGAWelcomeListener;
import cn.sirius.nga.properties.NGAWelcomeProperties;
import cn.sirius.nga.properties.NGAdController;

import static cn.sirius.nga.NGASDKFactory.getNGASDK;

/**
 * Created by caokun on 2019/7/23 11:45.
 */

public class SUcAdSdk implements ISGameSDK {

    private String TAG = "GameSdkLog";

    private static SUcAdSdk sUcAdSdk;
    private InitCallback mSAdSDKInitCallback;
    private String adAppId;

    public static SUcAdSdk getImpl() {
        if (sUcAdSdk == null) {
            synchronized (SUcAdSdk.class) {
                if (sUcAdSdk == null) {
                    sUcAdSdk = new SUcAdSdk();
                }
            }
        }
        return sUcAdSdk;
    }

    private Activity mActivity;
    private Handler mHandler;

    private AdCallback mAdCallback;

    private String welcomeId;
    private String bannerPosId;
    private String insertPosId;
    private String videoPosId;

    private NGAVideoController mNGAVideoController;

    @Override
    public void init(Context context, Config config, InitCallback callback) {
        this.mSAdSDKInitCallback = callback;
        this.mActivity = (Activity) context;
        this.mHandler = new Handler(Looper.getMainLooper());



        AdConfigInfo adConfigInfo = config.getAdConfigInfo();
        String bannerPosId = adConfigInfo.getBannerPosId();
        String insertPosId = adConfigInfo.getInsertPosId();
        String videoPosId = adConfigInfo.getVideoPosId();
        String welcomeId = adConfigInfo.getWelcomeId();
        String adId = adConfigInfo.getAdId();
        this.adAppId = adId;

        if (TextUtils.isEmpty(adAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在String.xml文件文件配置广告appId  5  code:00045");
            return;
        }
        if (TextUtils.isEmpty(bannerPosId) || TextUtils.isEmpty(insertPosId) || TextUtils.isEmpty(videoPosId) || TextUtils.isEmpty(welcomeId)) {
            ToastUtil.show(TAG, "初始化失败，请在String.xml文件文件配置广告PosId  6  code:00055");
            return;
        }

        this.bannerPosId = bannerPosId;
        this.insertPosId = insertPosId;
        this.videoPosId = videoPosId;
        this.welcomeId = welcomeId;

        initAd(mActivity, adId);
    }

    private void initAd(Activity activity, String adAppId) {
        if (TextUtils.isEmpty(adAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在清单文件配置广告appId  7 code:00045");
            return;
        }
        NGASDK ngasdk = NGASDKFactory.getNGASDK();
        Map<String, Object> args = new HashMap<>();
        args.put(NGASDK.APP_ID, adAppId);
        ngasdk.init(activity, args, mInitCallback);
    }

    private NGASDK.InitCallback mInitCallback = new NGASDK.InitCallback() {  //初始化
        @Override
        public void success() {
            Log.d(TAG, "initSGameSDK initAd: success");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mSAdSDKInitCallback != null) {
                        mSAdSDKInitCallback.onSuccess();
                    }
                }
            });
        }

        @Override
        public void fail(Throwable throwable) {
            Log.d(TAG, "initSGameSDK initAd: fail");
            if (mSAdSDKInitCallback != null) {
                AdError error = new AdError();
                error.setCode(String.valueOf(AdError.AD_INIT_ERROR));
                error.setThrowable(throwable);
                mSAdSDKInitCallback.onFailure(error);
            }
        }
    };

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        this.mAdCallback = callback;
        switch (type) {
            case BANNER:
                loadBannerAd((Activity) context);
                break;
            case VIDEO:
                loadVideoAd((Activity) context);
                break;
            case INSTER:
                loadInsertAd((Activity) context);
                break;
            case SPLASH:
                Activity activity = (Activity) context;
                ViewGroup viewGroup = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                loadSplashAd(activity, viewGroup);
                break;
        }
    }

    /**
     * 插屏广告
     * 部分广点通的插屏是只能竖屏的,广点通那边的问题
     */
    private void loadInsertAd(Activity activity) {  //插屏广告
        if (TextUtils.isEmpty(adAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在清单文件配置广告appId  8  code:00045");
            return;
        }
//        NGAInsertProperties properties = new NGAInsertProperties(activity, AdConfig.appId, AdConfig.insertPosId, null);
        NGAInsertProperties properties = new NGAInsertProperties(activity, adAppId, insertPosId, null);
        properties.setListener(mInsertAdListener);
        NGASDK ngasdk = NGASDKFactory.getNGASDK();
        ngasdk.loadAd(properties);
    }

    NGAInsertListener mInsertAdListener = new NGAInsertListener() {

        @Override
        public void onShowAd() {
            Log.d(TAG, "InsertAdListener onShowAd:  ");
        }


        @Override
        public void onRequestAd() {
            Log.d(TAG, "InsertAdListener onRequestAd:  ");
        }

        @Override
        public <T extends NGAdController> void onReadyAd(T controller) {
            NGAInsertController ngaInsertController = (NGAInsertController) controller;
            Log.d(TAG, "InsertAdListener onReadyAd:  ngaInsertController " + ngaInsertController);
            if (ngaInsertController != null) {
                ngaInsertController.showAd();
                if (mAdCallback != null) {
                    mAdCallback.onPresent();
                }
            }
        }

        @Override
        public void onCloseAd() {
            if (mAdCallback != null) {
                mAdCallback.onDismissed();
            }
        }

        @Override
        public void onClickAd() {
            if (mAdCallback != null) {
                mAdCallback.onClick();
            }
        }

        @Override
        public void onErrorAd(int code, String message) {
            Log.d(TAG, "InsertAdListener onErrorAd: code " + code + " message " + message);
            if (mAdCallback != null) {
                AdError error = new AdError();
                error.setCode(String.valueOf(AdError.AD_ERROR));
//                error.setCode(String.valueOf(code));
                error.setMessage(message);
                mAdCallback.onNoAd(error);
            }
        }
    };


    private RelativeLayout mBannerView;
    private ViewManager mWindowManager;

    private void loadBannerAd(Activity activity) {
        if (TextUtils.isEmpty(adAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在清单文件配置广告appId  12 code:00045");
            return;
        }
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

        Log.d(TAG, "loadBannerAd: adAppId " + adAppId + " bannerPosId " + bannerPosId);

        NGABannerProperties properties = new NGABannerProperties(activity, adAppId, bannerPosId, mBannerView);
        properties.setListener(mBannerAdListener);
        NGASDK ngasdk = NGASDKFactory.getNGASDK();
        ngasdk.loadAd(properties);
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
            if (mAdCallback != null) {
                mAdCallback.onPresent();
            }
        }

        @Override
        public void onCloseAd() {
            //广告关闭之后mController置null，鼓励加载广告重新调用loadAd，提高广告填充率
            mNGAVideoController = null;
            if (mAdCallback != null) {
                mAdCallback.onDismissed();
            }
        }

        @Override
        public void onErrorAd(int code, String message) {
            ToastUtil.show(TAG, "onErrorAd errorCode:" + code + ", message:" + message);
            if (mAdCallback != null) {
                AdError error = new AdError();
                error.setCode(String.valueOf(AdError.AD_ERROR));
//                error.setCode(String.valueOf(code));
                error.setMessage(message);
                mAdCallback.onNoAd(error);
            }
        }

        @Override
        public void onClickAd() {
//            ToastUtil.show(TAG, "onClickAd");
            if (mAdCallback != null) {
                mAdCallback.onClick();
            }
        }
    };

    private void loadVideoAd(Activity activity) {
        if (TextUtils.isEmpty(adAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在清单文件配置广告appId 13   code:00045");
            return;
        }
        NGAVideoProperties properties = new NGAVideoProperties(activity, adAppId, videoPosId);
        properties.setListener(mVideoAdListener);
        NGASDK ngasdk = NGASDKFactory.getNGASDK();
        ngasdk.loadAd(properties);
    }

    public void loadSplashAd(Activity activity, ViewGroup container) {
        if (TextUtils.isEmpty(adAppId)) {
            ToastUtil.show(TAG, "初始化失败，请在清单文件配置广告appId 14  code:00045");
            return;
        }
        NGAWelcomeProperties properties = new NGAWelcomeProperties(activity, adAppId, welcomeId, container);
        // 支持开发者自定义的跳过按钮。SDK要求skipContainer一定在传入后要处于VISIBLE状态，且其宽高都不得小于3x3dp。
        // 如果需要使用SDK默认的跳过按钮，可以选择上面两个构造方法。
        //properties.setSkipView(skipView);
        properties.setListener(mSplashAdListener);
        NGASDK ngasdk = getNGASDK();
        ngasdk.loadAd(properties);
    }

    NGAVideoListener mVideoAdListener = new NGAVideoListener() {
        @Override
        public void onRequestAd() {  //01 请求
            Log.d(TAG, "VideoAdListener onRequestAd: 05");
        }

        @Override
        public <T extends NGAdController> void onReadyAd(T controller) {  //02 准备完成
            Log.d(TAG, "VideoAdListener onReadyAd: 06");
            mNGAVideoController = (NGAVideoController) controller;
            if (mNGAVideoController != null) {  //播放视频
                mNGAVideoController.showAd();
                if (mAdCallback != null) {
                    mAdCallback.onPresent();
                }
            }
        }

        @Override
        public void onShowAd() { // 03 播放
            Log.d(TAG, "VideoAdListener onShowAd: 01");
        }

        @Override
        public void onCompletedAd() {  //04 播放完成
            Log.d(TAG, "VideoAdListener onCompletedAd: 07");
        }


        @Override
        public void onCloseAd() {  //05 关闭
            Log.d(TAG, "VideoAdListener onCloseAd: 03");
            if (mAdCallback != null) {
                mAdCallback.onDismissed();
            }
        }

        @Override
        public void onClickAd() {
            Log.d(TAG, "VideoAdListener onClickAd: 02");
            if (mAdCallback != null) {
                mAdCallback.onClick();
            }
        }

        @Override
        public void onErrorAd(final int code, final String message) {
            Log.d(TAG, "VideoAdListener onErrorAd: 04" + " ucsdk_code " + code + "  message " + message);
//            ToastUtil.show(TAG, "onErrorAd " + " code " + code + "  message " + message);
            if (mAdCallback != null) {
                AdError adError = new AdError();
                adError.setCode(String.valueOf(AdError.AD_ERROR));
//                adError.setCode(String.valueOf(code));
                adError.setMessage(message);
                mAdCallback.onNoAd(adError);
            }
        }
    };

    //注意：请在Activity成员变量保存，使用匿名内部类可能导致回收
    private NGAWelcomeListener mSplashAdListener = new NGAWelcomeListener() {

        @Override
        public void onClickAd() {
            Log.d(TAG, "SplashAdListener onClickAd: 05");
            if (mAdCallback != null) {
                mAdCallback.onClick();
            }
        }

        @Override
        public void onErrorAd(int code, String message) {
            Log.d(TAG, "SplashAdListener onErrorAd: 06" + " code " + code + "  message " + message); //code 8201  message [5004-没有广告]
            if (mAdCallback != null) {
                AdError adError = new AdError();
                adError.setCode(String.valueOf(AdError.AD_ERROR));
//                adError.setCode(String.valueOf(code));
                adError.setMessage(message);
                mAdCallback.onNoAd(adError);
            }
        }

        @Override
        public void onShowAd() {
            Log.d(TAG, "SplashAdListener onShowAd: 03");
            // 广告展示后一定要把预设的开屏图片隐藏起来
            if (mAdCallback != null) {
                mAdCallback.onPresent();
            }
        }

        @Override
        public void onCloseAd() {
            Log.d(TAG, "SplashAdListener onCloseAd: 04");
            //无论成功展示成功或失败都回调用该接口，所以开屏结束后的操作在该回调中实现
            if (mAdCallback != null) {
                mAdCallback.onDismissed();
            }
        }

        @Override
        public <T extends NGAdController> void onReadyAd(T controller) {  //不回调此方法
            Log.d(TAG, "SplashAdListener onReadyAd: 02");
            // 开屏广告是闪屏过程自动显示不需要NGAdController对象，所以返回controller为null；
//            ToastUtil.show(TAG, "onReadyAd");
        }

        @Override
        public void onRequestAd() {
            Log.d(TAG, "SplashAdListener onRequestAd: 01");
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
            Log.d(TAG, "SplashAdListener onTimeTickAd: 07 剩余毫秒数 millisUntilFinished " + millisUntilFinished);
        }
    };


}
