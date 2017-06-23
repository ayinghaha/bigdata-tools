package com.iflytek.voicecloud.itm.dao.analysis;

import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/12
 */
public interface OverAllDao {

    /**
     * 获取overall表 web数据一览
     * @param condition     查询条件
     * @return  web数据一览
     */
    OverAllData getOverviewData(Map<String, Object> condition);

    /**
     * 获取web数据趋势图数据
     * @param condition     查询条件
     * @return      时间段内每日/每小时数据
     */
    List<OverAllData> getOverAllDataTrend(Map<String, Object> condition);

}
