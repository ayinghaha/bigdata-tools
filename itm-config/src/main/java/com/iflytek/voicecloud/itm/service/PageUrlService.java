package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.analysis.PageUrl;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/27
 */
public interface PageUrlService {

    List<PageUrl> pageUrlAnalysis(Map<String, Object> condition);

    List<PageUrl> pageUrlAnalysisTrend(Map<String, Object> condition);
}
