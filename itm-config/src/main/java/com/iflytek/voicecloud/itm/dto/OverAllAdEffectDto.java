package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/26
 */
public class OverAllAdEffectDto  {

    public static Map<String, Object> formatAdEffectDataJson(OverAllData overAllData) {

        Map<String, Object> resMap = new HashMap<String, Object>();
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (overAllData != null ) {
            resMap.put("startTime", dateFormate.format(overAllData.getStartTime()));
            resMap.put("pv", overAllData.getPv());
            resMap.put("vv", overAllData.getVv());
            resMap.put("avg_detention_time", overAllData.getAvgDetentionTime());
            resMap.put("avg_page_depth", overAllData.getAvgPageDepth());
            resMap.put("depth_over_2", overAllData.getDepthOver2());
            resMap.put("depth1", overAllData.getDepth1());
        } else {
            resMap.put("startTime", "0000-00-00 00:00:00");
            resMap.put("pv", 0);
            resMap.put("vv", 0);
            resMap.put("avg_detention_time", 0);
            resMap.put("avg_page_depth", 0);
            resMap.put("depth_over_2", 0);
            resMap.put("depth1", 0);
        }

        return resMap;
    }


}
