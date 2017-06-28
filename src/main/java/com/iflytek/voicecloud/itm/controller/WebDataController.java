package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.dto.OverAllAdEffectDto;
import com.iflytek.voicecloud.itm.dto.OverAllDto;
import com.iflytek.voicecloud.itm.dto.PageUrlDto;
import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;
import com.iflytek.voicecloud.itm.entity.analysis.PageUrl;
import com.iflytek.voicecloud.itm.service.PageUrlService;
import com.iflytek.voicecloud.itm.service.WebDataService;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    @Autowired
    PageUrlService pageUrlService;

    /**
     * 数据来源终端类型
     */
    private static String[] webTypeArray = {"pc", "pad", "phone", "other", "all"};

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
        Map<String, Object> condition = getQueryTimeCondition(queryTime, false);
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
        Map<String, Object> condition = getQueryTimeCondition(queryTime, false);
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
     * 广告效果
     * @param request   request
     * @param response  response
     * @throws Exception    异常
     */
    @RequestMapping("/adeffect")
    public void adEffect(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String containerId = request.getParameter("containerId");
        String webType = request.getParameter("webType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String queryTime = request.getParameter("queryTime");

        // 查询条件
        Map<String, Object> condition = new HashMap<String, Object>();

        if (containerId == null || webType == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        } else if (queryTime != null) {
            condition = getQueryTimeCondition(queryTime, false);
            condition.put("queryTime", queryTime);
        } else if (startDate != null && endDate != null) {
            condition.put("startTime", startDate + " 00:00:00");
            condition.put("endTime", endDate + " 23:00:00");
        } else {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不正确"));
            return ;
        }
        condition.put("containerId", containerId);
        condition.put("webType", webType);

        OverAllData overviewData = webDataService.getOverAllData(condition);
        if (overviewData == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "查询数据为空"));
            return;
        }

        Map<String, Object> result = new HashMap<String, Object>();

        // 查询 昨日 今日 上周 上月 粒度时，需要返回环比数据
        if (queryTime != null) {
            Map<String, Object> relativeRatiocondition = getQueryTimeCondition(queryTime, true);
            // queryTime 为今日时查询小时数据，顾需要修改为昨日
            String relativeQueryTime = queryTime.equals("today") ? "yesterday" : queryTime;
            relativeRatiocondition.put("queryTime", relativeQueryTime);
            relativeRatiocondition.put("containerId", containerId);
            relativeRatiocondition.put("webType", webType);
            OverAllData relativeRatioData = webDataService.getOverAllData(relativeRatiocondition);

            result.put("currentRatio", OverAllAdEffectDto.formatAdEffectDataJson(overviewData));
            result.put("relativeRatio", OverAllAdEffectDto.formatAdEffectDataJson(relativeRatioData));
        } else {
            // 自由时间段，不需要返回环比数据，但是需要重新计算各个指标
            OverAllData freeTimeData = new OverAllData();
            freeTimeData.setPv(overviewData.getPv());
            freeTimeData.setVv(overviewData.getVv());
            freeTimeData.setAvgDetentionTime(overviewData.getAvgDetentionTime() / overviewData.getVv());
            freeTimeData.setAvgPageDepth(overviewData.getAvgPageDepth() / overviewData.getVv());
            freeTimeData.setDepth1(overviewData.getDepth1() / overviewData.getVv());
            freeTimeData.setDepthOver2(overviewData.getDepthOver2() / overviewData.getVv());
            freeTimeData.setStartTime(overviewData.getStartTime());

            result.put("currentRatio", OverAllAdEffectDto.formatAdEffectDataJson(freeTimeData));
            result.put("relativeRatio", null);
        }

        ResponseUtil.setResponseJson(response, new Message(1, result));
        return ;
    }

    /**
     * 广告效果趋势图
     * @param request   request
     * @param response  response
     * @throws Exception    异常
     */
    @RequestMapping("/adEffectTrend")
    public void adEffectTrend(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String containerId = request.getParameter("containerId");
        String webType = request.getParameter("webType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        // 自由时间段小于7天时可以查询小时数据 按天查询 day 按小时查询 hour
        String queryType = request.getParameter("queryType");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (containerId == null || webType == null || startDate == null || endDate == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        } else if ( (simpleDateFormat.parse(endDate).getTime() - simpleDateFormat.parse(startDate).getTime()) / 86400000 > 7 && queryType.equals("hour") ) {
            ResponseUtil.setResponseJson(response, new Message(-1, "查询时段禁止查询小时数据"));
            return ;
        }

        // 查询条件
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("containerId", containerId);
        condition.put("webType", webType);
        condition.put("startTime", startDate + " 00:00:00");
        condition.put("endTime", endDate + " 23:00:00");
        condition.put("queryType", queryType);

        List<OverAllData> adEffectDataList = webDataService.getAdEffectDataTrend(condition);
        if (adEffectDataList == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "查询数据为空"));
            return;
        }

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (OverAllData data : adEffectDataList) {
            resultList.add(OverAllAdEffectDto.formatAdEffectDataJson(data));
        }

        ResponseUtil.setResponseJson(response, new Message(1, resultList));
    }

    /**
     * 页面分析-网站页面
     * @param request   request
     * @param response  response
     * @throws Exception    异常
     */
    @RequestMapping("/pageurl")
    public void pageUrlAnalysis(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String containerId = request.getParameter("containerId");
        String webType = request.getParameter("webType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String queryTime = request.getParameter("queryTime");

        // 查询条件
        Map<String, Object> condition = new HashMap<String, Object>();

        if (containerId == null || webType == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        } else if (queryTime != null) {
            condition = getQueryTimeCondition(queryTime, false);
            condition.put("queryTime", queryTime);
        } else if (startDate != null && endDate != null) {
            condition.put("startTime", startDate + " 00:00:00");
            condition.put("endTime", endDate + " 23:00:00");
        } else {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不正确"));
            return ;
        }

        condition.put("containerId", containerId);
        condition.put("webType", webType);

        List<PageUrl> pageUrlDataList = pageUrlService.pageUrlAnalysis(condition);
        if (pageUrlDataList == null || pageUrlDataList.size() == 0) {
            ResponseUtil.setResponseJson(response, new Message(-1, "查询数据为空"));
            return ;
        }

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        float totalPv = 0;
        float totalVv = 0;
        for (PageUrl pageUrl : pageUrlDataList) {
            // 区分自由时段和固定时段，自由时段数据需要重新计算
            if (queryTime == null) {
                float freeDetentionTime = pageUrl.getAvgDetentionTime() / pageUrl.getVv();
                pageUrl.setAvgDetentionTime(freeDetentionTime);
            }
            result.add(PageUrlDto.formatPageUrlDataJson(pageUrl));
            totalPv += pageUrl.getPv();
            totalVv += pageUrl.getVv();
        }

        // 添加 pv 和 vv 占比
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (int i = 0; i < result.size(); i ++) {
            int vv = (Integer) result.get(i).get("vv");
            int pv = (Integer) result.get(i).get("pv");
            float vvRatio = vv / totalVv;
            float pvRatio = pv / totalPv;
            result.get(i).put("vvRatio", decimalFormat.format(vvRatio));
            result.get(i).put("pvRatio", decimalFormat.format(pvRatio));
        }

        ResponseUtil.setResponseJson(response, new Message(1, result));
    }

    /**
     * 指定url的数据趋势图
     * @param request       request
     * @param response      response
     * @throws Exception    异常
     */
    @RequestMapping("/urlTrend")
    public void pageUrlTrend(HttpServletRequest request, HttpServletResponse response) throws Exception {

         String containerId = request.getParameter("containerId");
         String webType = request.getParameter("webType");
         String startDate = request.getParameter("startDate");
         String endDate = request.getParameter("endDate");
         String pageUrl = request.getParameter("pageUrl");

        if (containerId == null || webType == null || pageUrl == null || startDate == null || endDate == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("containerId", containerId);
        condition.put("webType", webType);
        condition.put("startTime", startDate + " 00:00:00");
        condition.put("endTime", endDate + " 23:00:00");
        condition.put("pageUrl", pageUrl);

        List<PageUrl> pageUrls = pageUrlService.pageUrlAnalysisTrend(condition);
        if (pageUrls == null || pageUrls.size() == 0) {
            ResponseUtil.setResponseJson(response, new Message(-1, "查询数据为空"));
            return;
        }

        // 遍历结果
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (PageUrl page : pageUrls) {
            result.add(PageUrlDto.formatPageUrlDataJson(page));
        }

        ResponseUtil.setResponseJson(response, new Message(1, result));
    }

    /**
     * 根据查询类型时间获取查询条件
     * @param queryTime     string "today", "yesterday", "lastWeek", "lastMonth"
     * @param relativeRatio  是否为环比时间
     * @return      查询条件
     */
    private Map<String, Object> getQueryTimeCondition(String queryTime, boolean relativeRatio) {
        // 日期查询条件
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Map<String, Object> condition = new HashMap<String, Object>();
        if (queryTime.equals("today") || queryTime.equals("yesterday")) {
            // 获取昨日或今日的起始时间
            if (queryTime.equals("yesterday")) {
                calendar.add(Calendar.DATE, -1);
            }

            // 环比再减去1天
            if (relativeRatio) {
                calendar.add(Calendar.DATE, -1);
            }

            String localDate = dateFormate.format(calendar.getTime());
            condition.put("startTime", localDate + " 00:00:00");
            condition.put("endTime", localDate + " 23:00:00");
        } else if (queryTime.equals("lastWeek")) {
            // 获取上周第一天和最后一天 美国计时是一周日为开始, 所以获取SunDay的时候，会回到上周
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            condition.put("endTime", dateFormate.format(calendar.getTime()) + " 23:00:00");
            calendar.add(Calendar.DATE, -7);

            // 环比则取上上周时间
            if (relativeRatio) {
                calendar.add(Calendar.DATE, -7);
            }

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            condition.put("startTime", dateFormate.format(calendar.getTime()) + " 00:00:00");
        } else if (queryTime.equals("lastMonth")) {
            // 获取上月的第一天和最后一天
            calendar.add(Calendar.MONTH, -1);

            // 环比取上上月时间
            if (relativeRatio) {
                calendar.add(Calendar.MONTH, -1);
            }

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            condition.put("startTime", dateFormate.format(calendar.getTime()) + " 00:00:00");
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            condition.put("endTime", dateFormate.format(calendar.getTime()) + " 23:00:00");
        }

        return condition;
    }
}
