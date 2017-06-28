package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.analysis.PageUrl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/28
 */
public class PageUrlDto {

    public static Map<String, Object> formatPageUrlDataJson(PageUrl pageUrl) {

        Map<String, Object> resMap = new HashMap<String, Object>();
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        resMap.put("startTime", dateFormate.format(pageUrl.getStartTime()));
        resMap.put("pv", pageUrl.getPv());
        resMap.put("vv", pageUrl.getVv());
        resMap.put("pageUrl", pageUrl.getPageUrl());
        resMap.put("avgDetentionTime", pageUrl.getAvgDetentionTime());

        return resMap;
    }
}
