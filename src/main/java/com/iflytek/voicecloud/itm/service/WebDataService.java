package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/6/12
 */
public interface WebDataService {

    OverAllData getOverAllData(Map<String, Object> condition);

    List<OverAllData> getOverAllDataTrend(Map<String, Object> condition);

}
