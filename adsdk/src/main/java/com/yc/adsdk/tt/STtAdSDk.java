package com.yc.adsdk.tt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTInteractionAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.yc.adsdk.R;
import com.yc.adsdk.core.AdCallback;
import com.yc.adsdk.core.AdType;
import com.yc.adsdk.core.Config;
import com.yc.adsdk.core.Error;
import com.yc.adsdk.core.ISGameSDK;
import com.yc.adsdk.core.InitCallback;
import com.yc.adsdk.tt.config.TTAdManagerHolder;
import com.yc.adsdk.tt.config.TTConfig;
import com.yc.adsdk.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/7/16.
 */

public class STtAdSDk implements ISGameSDK {

    private static final String TAG = "STtAdSDk";

    private static STtAdSDk sTtAdSDk;
    private TTAdNative mTTAdNative;
    private ViewGroup mSplashContainer;

    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3000;
    private static final int MSG_GO_MAIN = 1;

    //开屏广告是否已经加载
    private boolean mHasLoaded;

    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。

    private AdCallback mAdCallback;
    private InitCallback mInitCallback;
    private Context mShowAdContext;
    private Config mConfig;


    public static STtAdSDk getImpl() {
        if (sTtAdSDk == null) {
            synchronized (STtAdSDk.class) {
                if (sTtAdSDk == null) {
                    sTtAdSDk = new STtAdSDk();
                }
            }
        }
        return sTtAdSDk;
    }

    @Override
    public void init(Context context, Config config) {
        init(context, config, null);
    }

    @Override
    public void init(Context context, Config config, InitCallback callback) {
        TTAdManagerHolder.init(context);
        mConfig = config;
        mInitCallback = callback;
    }

    @Override
    public void showAd(Context context, AdType type, AdCallback callback) {
        TTConfig ttConfig = (TTConfig) mConfig.getExt();
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
        mAdCallback = callback;
        mShowAdContext = context;
        Activity activity;
        switch (type) {
            case BANNER:
                activity = (Activity) context;
                mSplashContainer = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                loadBannerAd(ttConfig.getTtAdbanner());
                break;
            case BANNER_DOWNLOAD:
                activity = (Activity) context;
                mSplashContainer = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                loadBannerAd(ttConfig.getTtAdbannerDownload());
                break;
            case BANNER_NATIVE:
                activity = (Activity) context;
                mSplashContainer = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                loadNativeBannerAd(ttConfig.getTtAdbannerNative());
                break;
            case VIDEO_NATIVE:
                activity = (Activity) context;
                mSplashContainer = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                loadNativeAd(ttConfig.getTtAdVideoNative());
                break;
            case VIDEO_V:
                loadVideoAd(ttConfig.getTtAdVideoHorizontal(), TTAdConstant.VERTICAL);
                break;
            case VIDEO:
                loadVideoAd(ttConfig.getTtAdVideoVertical(), TTAdConstant.HORIZONTAL);
                break;
            case INSTER_DOWNLOAD:
                loadInteractionAd(ttConfig.getTtAdInsterDownload());
            case INSTER:
                loadInteractionAd(ttConfig.getTtAdInsterNormal());
                break;
            case SPLASH:
                //定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
                mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);
                activity = (Activity) context;
                mSplashContainer = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                loadSplashAd(ttConfig.getTtAdSplash());  // 加载开屏广告
                break;
        }
    }

    private Button mCreativeButton;

    /**
     * 加载原生Banner广告
     */
    @SuppressWarnings({"ALL", "SameParameterValue"})
    private void loadNativeBannerAd(String codeId) {
        //step4:创建广告请求参数AdSlot,注意其中的setNativeAdtype方法，具体参数含义参考文档
        final AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 257)
                .setNativeAdType(AdSlot.TYPE_BANNER) //请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
                .setAdCount(1)
                .build();

        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeAd(adSlot, new TTAdNative.NativeAdListener() {
            @Override
            public void onError(int code, String message) {
                showToast("原生Banner " + "load error : " + code + ", " + message);
            }

            @Override
            public void onNativeAdLoad(List<TTNativeAd> ads) {
                if (ads.get(0) == null) {
                    return;
                }
                View bannerView = LayoutInflater.from(mShowAdContext).inflate(R.layout.native_ad, mSplashContainer, false);
                if (bannerView == null) {
                    return;
                }
                if (mCreativeButton != null) {
                    //防止内存泄漏
                    mCreativeButton = null;
                }
                mSplashContainer.removeAllViews();
                mSplashContainer.addView(bannerView);
                //绑定原生广告的数据
                setAdData(bannerView, ads.get(0));
            }
        });
    }

    @SuppressWarnings("RedundantCast")
    private void setAdData(View nativeView, TTNativeAd nativeAd) {
        ((TextView) nativeView.findViewById(R.id.tv_native_ad_title)).setText(nativeAd.getTitle());
        ((TextView) nativeView.findViewById(R.id.tv_native_ad_desc)).setText(nativeAd.getDescription());
        ImageView imgDislike = nativeView.findViewById(R.id.img_native_dislike);
        bindDislikeAction(nativeAd, imgDislike);
        if (nativeAd.getImageList() != null && !nativeAd.getImageList().isEmpty()) {
            TTImage image = nativeAd.getImageList().get(0);
            if (image != null && image.isValid()) {
                ImageView im = nativeView.findViewById(R.id.iv_native_image);
                Glide.with(mShowAdContext).load(image.getImageUrl()).into(im);
            }
        }
        TTImage icon = nativeAd.getIcon();
        if (icon != null && icon.isValid()) {
            ImageView im = nativeView.findViewById(R.id.iv_native_icon);
            Glide.with(mShowAdContext).load(icon.getImageUrl()).into(im);
        }
        mCreativeButton = (Button) nativeView.findViewById(R.id.btn_native_creative);
        //可根据广告类型，为交互区域设置不同提示信息
        switch (nativeAd.getInteractionType()) {
            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                nativeAd.setActivityForDownloadApp((Activity) mShowAdContext);
                mCreativeButton.setVisibility(View.VISIBLE);
                nativeAd.setDownloadListener(mDownloadListener); // 注册下载监听器
                break;
            case TTAdConstant.INTERACTION_TYPE_DIAL:
                mCreativeButton.setVisibility(View.VISIBLE);
                mCreativeButton.setText("立即拨打");
                break;
            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
            case TTAdConstant.INTERACTION_TYPE_BROWSER:
                mCreativeButton.setVisibility(View.VISIBLE);
                mCreativeButton.setText("查看详情");
                break;
            default:
                mCreativeButton.setVisibility(View.GONE);
                showToast("原生Banner " + "交互类型异常");
        }

        //可以被点击的view, 也可以把nativeView放进来意味整个广告区域可被点击
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(nativeView);

        //触发创意广告的view（点击下载或拨打电话）
        List<View> creativeViewList = new ArrayList<>();
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
        //creativeViewList.add(nativeView);
        creativeViewList.add(mCreativeButton);

        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
        nativeAd.registerViewForInteraction((ViewGroup) nativeView, clickViewList, creativeViewList, imgDislike, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                if (ad != null) {
                    showToast("原生Banner " + "广告" + ad.getTitle() + "被点击");
                }
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                    showToast("原生Banner " + "广告" + ad.getTitle() + "被创意按钮被点击");
                }
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                    showToast("原生Banner " + "广告" + ad.getTitle() + "展示");
                }
            }
        });
    }

    //接入网盟的dislike 逻辑，有助于提示广告精准投放度
    private void bindDislikeAction(TTNativeAd ad, View dislikeView) {
        final TTAdDislike ttAdDislike = ad.getDislikeDialog((Activity) mShowAdContext);
        if (ttAdDislike != null) {
            ttAdDislike.setDislikeInteractionCallback(new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onSelected(int position, String value) {
                    mSplashContainer.removeAllViews();
                }

                @Override
                public void onCancel() {

                }
            });
        }
        dislikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttAdDislike != null)
                    ttAdDislike.showDislikeDialog();
            }
        });
    }

    private final TTAppDownloadListener mDownloadListener = new TTAppDownloadListener() {
        @Override
        public void onIdle() {
            if (mCreativeButton != null) {
                mCreativeButton.setText("开始下载");
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                if (totalBytes <= 0L) {
                    mCreativeButton.setText("下载中 percent: 0");
                } else {
                    mCreativeButton.setText("下载中 percent: " + (currBytes * 100 / totalBytes));
                }
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("下载暂停 percent: " + (currBytes * 100 / totalBytes));
            }
        }

        @Override
        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("重新下载");
            }
        }

        @Override
        public void onInstalled(String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("点击打开");
            }
        }

        @Override
        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
            if (mCreativeButton != null) {
                mCreativeButton.setText("点击安装");
            }
        }
    };


    /**
     * 加载Banner广告
     */
    private void loadBannerAd(String codeId) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 257)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerAd(adSlot, new TTAdNative.BannerAdListener() {

            @Override
            public void onError(int code, String message) {
                showToast("Banner展示 " + "load error : " + code + ", " + message);
                Log.d(TAG, "onError: " + "load error : " + code + ", " + message);
                mSplashContainer.removeAllViews();
            }

            @Override
            public void onBannerAdLoad(final TTBannerAd ad) {
                if (ad == null) {
                    return;
                }
                View bannerView = ad.getBannerView();
                if (bannerView == null) {
                    return;
                }
                //设置轮播的时间间隔  间隔在30s到120秒之间的值，不设置默认不轮播
                ad.setSlideIntervalTime(30 * 1000);
                mSplashContainer.removeAllViews();
                mSplashContainer.addView(bannerView);
                //设置广告互动监听回调
                ad.setBannerInteractionListener(new TTBannerAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        showToast("Banner展示 " + "广告被点击");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        showToast("Banner展示 " + "广告展示");
                    }
                });
                //（可选）设置下载类广告的下载监听
                bindDownloadListener(ad);
                //在banner中显示网盟提供的dislike icon，有助于广告投放精准度提升
                ad.setShowDislikeIcon(new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onSelected(int position, String value) {
                        showToast("Banner展示 " + "点击 " + value);
                        //用户选择不喜欢原因后，移除广告展示
                        mSplashContainer.removeAllViews();
                    }

                    @Override
                    public void onCancel() {
                        showToast("Banner展示 " + "点击取消 ");
                    }
                });

                //获取网盟dislike dialog，您可以在您应用中本身自定义的dislike icon 按钮中设置 mTTAdDislike.showDislikeDialog();
                /*mTTAdDislike = ad.getDislikeDialog(new TTAdDislike.DislikeInteractionCallback() {
                        @Override
                        public void onSelected(int position, String value) {
                              showToast("Banner展示 "+ "点击 " + value);
                        }

                        @Override
                        public void onCancel() {
                              showToast("Banner展示 "+ "点击取消 ");
                        }
                    });
                if (mTTAdDislike != null) {
                    XXX.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTTAdDislike.showDislikeDialog();
                        }
                    });
                } */

            }
        });
    }

    private boolean mHasShowDownloadActive = false;

    private void bindDownloadListener(TTBannerAd ad) {
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                showToast("Banner展示 " + "点击图片开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    showToast("Banner展示 " + "下载中，点击图片暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                showToast("Banner展示 " + "下载暂停，点击图片继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                showToast("Banner展示 " + "下载失败，点击图片重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                showToast("Banner展示 " + "安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                showToast("Banner展示 " + "点击图片安装");
            }
        });
    }

    /**
     * 加载开屏视频广告
     */
    private void loadNativeAd(String codeId) {
        //step3:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)//开发者申请的广告位
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)//符合广告场景的广告尺寸
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        //step4:请求广告,对请求回调的广告作渲染处理
        mTTAdNative.loadDrawFeedAd(adSlot, new TTAdNative.DrawFeedAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d(TAG, message);
//                showToast(message);
                Error error = new Error();
                error.setTripartiteCode(code);
                error.setMessage(message);
                mAdCallback.onNoAd(error);
            }

            @Override
            public void onDrawFeedAdLoad(List<TTDrawFeedAd> ads) {
                if (ads == null || ads.isEmpty()) {
//                    TToast.show(NativeVerticalVideoActivity.this, " ad is null!");
//                    showToast(" ad is null!");
                    Error error = new Error();
                    error.setMessage("ad is null!");
                    mAdCallback.onNoAd(error);
                    return;
                }
                //为广告设置activity对象，下载相关行为需要该context对象
                for (TTDrawFeedAd ad : ads) {
                    ad.setActivityForDownloadApp((Activity) mShowAdContext);
                }
                //设置广告视频区域是否响应点击行为，控制视频暂停、继续播放，默认不响应；
                ads.get(0).setCanInterruptVideoPlay(true);
                //设置视频暂停的Icon和大小
                ads.get(0).setPauseIcon(BitmapFactory.decodeResource(mShowAdContext.getResources(), R.drawable.dislike_icon), 60);
                //获取广告视频播放的view并放入广告容器中
                mSplashContainer.addView(ads.get(0).getAdView());
                //初始化并绑定广告行为
                initAdViewAndAction(ads.get(0));
            }
        });
    }

    private void initAdViewAndAction(TTDrawFeedAd ad) {
        Button action = new Button(mShowAdContext);
        action.setText(ad.getButtonText());
        Button btTitle = new Button(mShowAdContext);
        btTitle.setText(ad.getTitle());

        int height = (int) dip2Px(mShowAdContext, 50);
        int margin = (int) dip2Px(mShowAdContext, 10);
        //noinspection SuspiciousNameCombination
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(height * 3, height);
        lp.gravity = Gravity.END | Gravity.BOTTOM;
        lp.rightMargin = margin;
        lp.bottomMargin = margin;
        mSplashContainer.addView(action, lp);

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(height * 3, height);
        lp1.gravity = Gravity.START | Gravity.BOTTOM;
        lp1.rightMargin = margin;
        lp1.bottomMargin = margin;
        mSplashContainer.addView(btTitle, lp1);

        //响应点击区域的设置，分为普通的区域clickViews和创意区域creativeViews
        //clickViews中的view被点击会尝试打开广告落地页；creativeViews中的view被点击会根据广告类型
        //响应对应行为，如下载类广告直接下载，打开落地页类广告直接打开落地页。
        //注意：ad.getAdView()获取的view请勿放入这两个区域中。
        List<View> clickViews = new ArrayList<>();
        clickViews.add(btTitle);
        List<View> creativeViews = new ArrayList<>();
        creativeViews.add(action);
        ad.registerViewForInteraction(mSplashContainer, clickViews, creativeViews, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
//                showToast("onAdClicked");
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
//                showToast("onAdCreativeClick");
                mAdCallback.onClick();
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
//                showToast("onAdShow");
                mAdCallback.onPresent();
            }
        });
    }

    private float dip2Px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }


    private TTFullScreenVideoAd mttFullVideoAd;

    /**
     * 加载视频广告
     */
    @SuppressWarnings("SameParameterValue")
    private void loadVideoAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(orientation)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
//                showToast("视频广告  "+message);
                Error error = new Error();
                error.setTripartiteCode(code);
                error.setMessage(message);
                mAdCallback.onNoAd(error);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                showToast("视频广告  " + "FullVideoAd loaded");
                mttFullVideoAd = ad;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        mAdCallback.onPresent();
//                        showToast("视频广告  "+"FullVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
//                        showToast("视频广告  "+"FullVideoAd bar click");
                        mAdCallback.onClick();
                    }

                    @Override
                    public void onAdClose() {
//                        showToast("视频广告  "+"FullVideoAd close");
                        mAdCallback.onDismissed();
                    }

                    @Override
                    public void onVideoComplete() {
//                        showToast("视频广告  "+"FullVideoAd complete");
                    }

                    @Override
                    public void onSkippedVideo() {
//                        showToast("视频广告  "+"FullVideoAd skipped");

                    }

                });
                if (mttFullVideoAd != null) {
                    //step6:在获取到广告后展示
                    mttFullVideoAd.showFullScreenVideoAd((Activity) mShowAdContext);
                    mttFullVideoAd = null;
                }
            }

            @Override
            public void onFullScreenVideoCached() {
                showToast("视频广告  " + "FullVideoAd video cached");
            }
        });
    }

    /**
     * 加载插屏广告
     */
    private void loadInteractionAd(String codeId) {
        //step4:创建插屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 600) //根据广告平台选择的尺寸，传入同比例尺寸
                .build();
        //step5:请求广告，调用插屏广告异步请求接口
        mTTAdNative.loadInteractionAd(adSlot, new TTAdNative.InteractionAdListener() {
            @Override
            public void onError(int code, String message) {
                showToast("code: " + code + "  message: " + message);
                Error error = new Error();
                error.setTripartiteCode(code);
                error.setMessage(message);
                mAdCallback.onNoAd(error);
            }

            @Override
            public void onInteractionAdLoad(final TTInteractionAd ttInteractionAd) {
//                showToast("type:  " + ttInteractionAd.getInteractionType());
                ttInteractionAd.setAdInteractionListener(new TTInteractionAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked() {
                        Log.d(TAG, "被点击");
//                        showToast("广告被点击");
                        mAdCallback.onClick();
                    }

                    @Override
                    public void onAdShow() {
                        Log.d(TAG, "被展示");
//                        showToast("广告被展示");
                        mAdCallback.onPresent();
                    }

                    @Override
                    public void onAdDismiss() {
                        Log.d(TAG, "插屏广告消失");
//                        showToast("广告消失");
                        mAdCallback.onDismissed();
                    }
                });
                //如果是下载类型的广告，可以注册下载状态回调监听
                if (ttInteractionAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ttInteractionAd.setDownloadListener(new TTAppDownloadListener() {
                        @Override
                        public void onIdle() {
                            Log.d(TAG, "点击开始下载");
//                            showToast("点击开始下载");
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d(TAG, "下载中");
//                            showToast("下载中");
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d(TAG, "下载暂停");
//                            showToast("下载暂停");
                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            Log.d(TAG, "下载失败");
//                            showToast("下载失败");
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                            Log.d(TAG, "下载完成");
//                            showToast("下载完成");
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                            Log.d(TAG, "安装完成");
//                            showToast("安装完成");
                        }
                    });
                }
                // 弹出插屏广告
                ttInteractionAd.showInteractionAd((Activity) mShowAdContext);
            }
        });
    }

    private WeakHandler mHandler;

    public STtAdSDk() {
        mHandler = new WeakHandler(mIHandler);
    }

    private WeakHandler.IHandler mIHandler = new WeakHandler.IHandler() {
        @Override
        public void handleMsg(Message msg) {
            if (msg.what == MSG_GO_MAIN) {
                if (!mHasLoaded) {
                    Error error = new Error();
                    error.setCode(String.valueOf(Error.AD_ERROR));
                    mAdCallback.onNoAd(error);
                }
            }
        }
    };

    /**
     * 加载开屏广告
     */
    private void loadSplashAd(String codeId) {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, message);
                mHasLoaded = true;
//                showToast(message);
//                goToMainActivity();

                Error error = new Error();
                error.setMessage(message);
                error.setCode(String.valueOf(Error.AD_ERROR));
                error.setTripartiteCode(code);
                mAdCallback.onNoAd(error);
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
//                showToast("开屏广告加载超时");
//                goToMainActivity();


                Error error = new Error();
                error.setCode(String.valueOf(Error.AD_ERROR));
                mAdCallback.onNoAd(error);
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d(TAG, "开屏广告请求成功");
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                mSplashContainer.removeAllViews();
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                mSplashContainer.addView(view);
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");
//                        showToast("开屏广告点击");

                        mAdCallback.onClick();
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
//                        showToast("开屏广告展示");

                        mAdCallback.onPresent();
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
//                        showToast("开屏广告跳过");

                        mAdCallback.onDismissed();


                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
//                        showToast("开屏广告倒计时结束");

                        mAdCallback.onDismissed();
                    }
                });
            }
        }, AD_TIME_OUT);
    }

    private void showToast(String msg) {
        ToastUtil.show(TAG, msg);
    }

}
