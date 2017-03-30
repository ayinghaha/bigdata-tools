package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.Trigger;
import com.iflytek.voicecloud.itm.entity.VariableFilter;
import com.iflytek.voicecloud.itm.entity.variable.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 格式化过滤器类型数据结果
 */
public class TriggerDto {

    public static Map<String, Object> formatTriggerJson(Trigger trigger) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("id", trigger.getId());
        resMap.put("name", trigger.getName());
        resMap.put("type", trigger.getType());
        resMap.put("delay", trigger.getDelay());

        // 添加filter列表返回结果
        List<VariableFilter> variableFilters = trigger.getVariableFilters();
        List<Map<String, Object>> filterMap = new ArrayList<Map<String, Object>>();
        for (VariableFilter variableFilter : variableFilters) {
            filterMap.add(FilterDto.formatFilterJson(variableFilter));
        }
        resMap.put("filters", filterMap);

        return resMap;
    }

}
