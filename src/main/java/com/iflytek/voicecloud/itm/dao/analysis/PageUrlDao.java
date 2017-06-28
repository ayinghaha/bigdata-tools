package com.iflytek.voicecloud.itm.dao.analysis;

import com.iflytek.voicecloud.itm.entity.analysis.PageUrl;
import sun.jvm.hotspot.debugger.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/27
 */
public interface PageUrlDao {

    /**
     * 网站页面数据查询
     * @param condition   查询条件
     * @return  返回数据 PageUrl
     */
    List<PageUrl> pageAnalysis(Map<String, Object> condition);

    /**
     * 网站页面数据趋势图
     * @param condition     查询条件
     * @return  返回数据
     */
    List<PageUrl> pageAnalysisTrend(Map<String, Object> condition);

}
