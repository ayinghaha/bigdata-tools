package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.analysis.OverAllDao;
import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;
import com.iflytek.voicecloud.itm.service.WebDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jdshao on 2017/6/12
 */
@Service
public class WebDataServiceImpl implements WebDataService {

    @Autowired
    private OverAllDao overAllDailyDao;

    public OverAllData getDailyData(Map<String, Object> condition) {
        return null;
    }


}
