package com.iflytek.voicecloud.itm.dao.analysis;

import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;

import java.util.Map;

/**
 * Created by jdshao on 2017/6/12
 */
public interface OverAllDao {

    /**
     * 获取overall日表数据
     * @param condition     查询条件
     * @return
     */
    OverAllData getOverViewData(Map<String, Object> condition);

    OverAllData getOverAllWeekly(Map<String, Object> condition);

}
