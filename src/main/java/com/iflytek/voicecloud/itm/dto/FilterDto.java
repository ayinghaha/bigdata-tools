package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.VariableFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * 格式化过滤器类型数据结果
 */
public class FilterDto {

    public static Map<String, Object> formatFilterJson(VariableFilter filter){

        Map<String, Object> resMap = new HashMap<String, Object>();

        // 设置父类成员变量
        resMap.put("variable", filter.getVariable().getName());
        resMap.put("variableId", filter.getVariable().getId());
        resMap.put("condition", filter.getCondition());
        resMap.put("value", filter.getValue());

        return resMap;
    }

}
