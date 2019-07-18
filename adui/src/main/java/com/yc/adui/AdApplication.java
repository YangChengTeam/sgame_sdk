package com.yc.adui;

import android.app.Application;

import com.yc.adsdk.core.Config;
import com.yc.adsdk.core.ISGameSDK;
import com.yc.adsdk.tt.STtAdSDk;
import com.yc.adsdk.tt.config.TTConfig;

/**
 * Created by mayn on 2019/7/16.
 */

public class AdApplication extends Application {

    private ISGameSDK mISGameSDK;



    @Override
    public void onCreate() {
        super.onCreate();


        mISGameSDK = STtAdSDk.getImpl();
        Config config = new Config();
        TTConfig ttConfig = new TTConfig();
        ttConfig.setTtAdInsterDownload("901121417");
        ttConfig.setTtAdInsterNormal("901121725");
        ttConfig.setTtAdSplash("801121648");
        ttConfig.setTtAdVideoHorizontal("901121375");
        ttConfig.setTtAdVideoVertical("901121375");
        ttConfig.setTtAdVideoNative("901121709");
        ttConfig.setTtAdbanner("901121987");
        ttConfig.setTtAdbannerDownload("901121895");
        ttConfig.setTtAdbannerNative("901121423");
        ttConfig.setTtAppName("APP测试媒体");
        config.setExt(ttConfig);
        config.setAppId("5001121");

        mISGameSDK.init(this, config);
    }
}
