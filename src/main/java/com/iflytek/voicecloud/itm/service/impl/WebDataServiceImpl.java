package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.analysis.OverAllDao;
import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;
import com.iflytek.voicecloud.itm.service.WebDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/12
 */
@Service
public class WebDataServiceImpl implements WebDataService {

    @Autowired
    private OverAllDao overAllDao;

    /**
     * 获取overall表 web数据一览
     * @param condition     查询条件
     * @return  web数据一览
     */
    public OverAllData getOverAllData(Map<String, Object> condition) {
        return overAllDao.getOverviewData(condition);
    }

    /**
     * 获取web数据趋势图数据
     * @param condition     查询条件
     * @return      时间段内每日/每小时数据
     */
    public List<OverAllData> getOverAllDataTrend(Map<String, Object> condition) {
        return overAllDao.getOverAllDataTrend(condition);
    }

    /**
     * 查询广告效果趋势
     * @param condition     查询条件
     * @return      时间段内每日/小时数据
     */
    public List<OverAllData> getAdEffectDataTrend(Map<String, Object> condition) {
        return overAllDao.getAdEffectDataTrend(condition);
    }


}
