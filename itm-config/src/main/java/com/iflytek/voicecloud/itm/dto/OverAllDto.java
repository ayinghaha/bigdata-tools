package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/21
 */
public class OverAllDto {

    public static Map<String, Object> formatOverAllDataJson(OverAllData overAllData) {

        Map<String, Object> resMap = new HashMap<String, Object>();
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (overAllData != null ) {
            resMap.put("startTime", dateFormate.format(overAllData.getStartTime()));
            resMap.put("pv", overAllData.getPv());
            resMap.put("uv", overAllData.getUv());
            resMap.put("uv_old", overAllData.getUvOld());
            resMap.put("ip_num", overAllData.getIpNum());
            resMap.put("uv", overAllData.getUv());
        } else {
            resMap.put("startTime", dateFormate.format(overAllData.getStartTime()));
            resMap.put("pv", 0);
            resMap.put("uv", 0);
            resMap.put("uv_old", 0);
            resMap.put("ip_num", 0);
            resMap.put("uv", 0);
        }

        return resMap;
    }
}
