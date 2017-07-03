package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.analysis.PageUrlDao;
import com.iflytek.voicecloud.itm.entity.analysis.PageUrl;
import com.iflytek.voicecloud.itm.service.PageUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/27
 */
@Service
public class PageUrlServiceImpl implements PageUrlService {

    @Autowired
    private PageUrlDao pageUrlDao;

    /**
     * 网站页面数据查询
     * @param condition   查询条件
     * @return  返回数据 PageUrl
     */
    public List<PageUrl> pageUrlAnalysis(Map<String, Object> condition) {
        return pageUrlDao.pageAnalysis(condition);
    }

    /**
     * 网站页面数据趋势图
     * @param condition     查询条件
     * @return  返回数据
     */
    public List<PageUrl> pageUrlAnalysisTrend(Map<String, Object> condition) {
        return pageUrlDao.pageAnalysisTrend(condition);
    }
}
