package com.iflytek.voicecloud.itm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.iflytek.voicecloud.itm.entity.VariableFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/2/23
 */
public class JsonUtil {

    /**
     * jackson转换对象
     */
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 对象转换为json串
     * @param object 传入对象
     * @return String 返回json串
     * @throws JsonProcessingException 异常
     */
    public static String ObjectToJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    /**
     * json串转换为List
     * @param json json串 {2,3,4}
     * @return List
     * @throws Exception 异常
     */
    public static List JsonToStringList(String json) throws Exception{
        return mapper.readValue(json, List.class);
    }

    /**
     * jons串转换为Filter列表
     * @param json  json串
     * @return      List<VariableFilter>
     * @throws Exception   异常
     */
    public static List<Map<String, Object>> JsonToFilterList(String json) throws Exception {
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Map.class);
        return mapper.readValue(json, listType);
    }

}
