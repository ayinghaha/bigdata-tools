package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.dto.OverAllDto;
import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;
import com.iflytek.voicecloud.itm.service.WebDataService;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 网站数据模块，数据分析接口
 */
@Controller
@RequestMapping("/webdata")
public class WebDataController {

    @Autowired
    WebDataService webDataService;

    /**
     * 数据来源终端类型
     */
    private static String[] webTypeArray = {"pc", "pad", "phone", "other"};

    /**
     * 查询时间类型
     */
    private static String[] queryTimeArray = {"today", "yesterday", "lastWeek", "lastMonth"};

    @RequestMapping("/overview")
    public void dataOverView(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String containerId = request.getParameter("containerId");
        String webType = request.getParameter("webType");
        String queryTime = request.getParameter("queryTime") != null ? request.getParameter("queryTime") : "today";

        // 检测参数
        if (containerId == null || webType == null || queryTime == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        } else if (!Arrays.asList(webTypeArray).contains(webType) || !Arrays.asList(queryTimeArray).contains(queryTime)) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数类型不正确"));
            return ;
        }

        // 查询条件
        Map<String, Object> condition = getQueryTimeCondition(queryTime);
        condition.put("containerId", containerId);
        condition.put("webType", webType);
        condition.put("queryTime", queryTime);
        OverAllData overviewData = webDataService.getOverAllData(condition);
        if (overviewData == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "查询数据为空"));
            return;
        }

        Message message = new Message(1, OverAllDto.formatOverAllDataJson(overviewData));
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/trend")
    public void getWebDataTrend(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String containerId = request.getParameter("containerId");
        String webType = request.getParameter("webType");
        String queryTime = request.getParameter("queryTime") != null ? request.getParameter("queryTime") : "today";

        // 检测参数
        if (containerId == null || webType == null || queryTime == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        } else if (!Arrays.asList(webTypeArray).contains(webType) || !Arrays.asList(queryTimeArray).contains(queryTime)) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数类型不正确"));
            return ;
        }

        // 查询条件
        Map<String, Object> condition = getQueryTimeCondition(queryTime);
        condition.put("containerId", containerId);
        condition.put("webType", webType);
        condition.put("queryTime", queryTime);
        List<OverAllData> dataList = webDataService.getOverAllDataTrend(condition);
        List<Map<String, Object>> resMap = new ArrayList<Map<String, Object>>();
        for (OverAllData overAllData : dataList) {
            resMap.add(OverAllDto.formatOverAllDataJson(overAllData));
        }

        ResponseUtil.setResponseJson(response, new Message(1, resMap));
    }

    /**
     * 根据查询类型时间获取查询条件
     * @param queryTime     string "today", "yesterday", "lastWeek", "lastMonth"
     * @return      查询条件
     */
    private Map<String, Object> getQueryTimeCondition(String queryTime) {
        // 日期查询条件
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Map<String, Object> condition = new HashMap<String, Object>();
        if (queryTime.equals("today") || queryTime.equals("yesterday")) {
            // 获取昨日或今日的起始时间
            if (queryTime.equals("yesterday")) calendar.add(Calendar.DATE, -1);
            String localDate = dateFormate.format(calendar.getTime());
            condition.put("startTime", localDate + " 00:00:00");
            condition.put("endTime", localDate + " 23:00:00");
        } else if (queryTime.equals("lastWeek")) {
            // 获取上周第一天和最后一天 美国计时是一周日为开始, 所以获取SunDay的时候，会回到上周
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            condition.put("endTime", dateFormate.format(calendar.getTime()) + " 23:00:00");
            calendar.add(Calendar.DATE, -7);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            condition.put("startTime", dateFormate.format(calendar.getTime()) + " 00:00:00");
        } else if (queryTime.equals("lastMonth")) {
            // 获取上月的第一天和最后一天
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            condition.put("startTime", dateFormate.format(calendar.getTime()) + " 00:00:00");
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            condition.put("endTime", dateFormate.format(calendar.getTime()) + " 23:00:00");
        }

        return condition;
    }

}
