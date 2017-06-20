package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.config.variable.Variable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 格式化变量类型数据结果
 */
public class VariableDto {

    /**
     * 反射对象路径
     */
    private static final String reflectPath = "com.iflytek.voicecloud.itm.entity.config.variable.";

    /**
     * 通过反射得到变量子类属性并放入Map中
     * @param variable  变量对象
     * @return  Map结果
     */
    public static Map<String, Object> formatVariableJson(Variable variable) throws Exception {

        Map<String, Object> resMap = new HashMap<String, Object>();

        // 设置父类成员变量
        resMap.put("id", variable.getId());
        resMap.put("name", variable.getName());
        resMap.put("type", variable.getType());

        String className = reflectPath + variable.getType();
        Class variableClass = Class.forName(className);
        Field[] fields = variableClass.getDeclaredFields();
        for (Field field : fields ) {
            resMap.put(field.getName(), field.get(variable));
        }

        return resMap;
    }

}
