package com.yc.ui.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guhb@ucweb.com on 2/23/16.
 */
public class AdConfig {

    public static String appId;
    public static String videoPosId;
    public static String bannerPosId;
    public static String insertPosId;
    public static String feedPosId;
    public static String welcomeId;
    public static String templateId;
    public static String templateId_2;
    public static String nativeId;

    static {
        appId = "1000004107";
        videoPosId = "1475893508926018";
        bannerPosId = "1476429649994173";
        insertPosId = "1476429649994172";
        feedPosId = "1476429598998171";
        welcomeId = "1498731130656389";
        templateId = "15198019565651178";
        templateId_2 = "1522136419571";
        nativeId = "1527652299758";
    }

    public static String toStringFormat() {
        Map<String, String> map = new HashMap<>();
        map.put("appId", appId);
        map.put("videoPosId", videoPosId);
        map.put("bannerPosId", bannerPosId);
        map.put("insertPosId", insertPosId);
        map.put("feedPosId", feedPosId);
        map.put("welcomeId", welcomeId);
        map.put("templateId", templateId);
        map.put("templateId_2", templateId_2);
        map.put("nativeId", nativeId);
        StringBuffer sb = new StringBuffer("Demo Config:\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
