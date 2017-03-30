package com.iflytek.voicecloud.itm.utils;

import com.iflytek.voicecloud.itm.Exception.ParamLackException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Field;

/**
 * 反射通用类
 */
public class ReflectionUtils {

    /**
     * 设置对象属性值
     * @param filed 属性字段
     * @param object 设置对象
     */
    public static void setFieldValue(Field filed, Object object, Object value) throws Exception{
        filed.set(object, value);
    }

    /**
     * 通过http请求装载相应的对象
     * @param className     全路径类名
     * @param object        装载对象
     * @param request       请求对象
     * @throws Exception    异常
     */
    public static Object loadObject(String className, Object object, HttpServletRequest request) throws Exception {
        Class objectClass = Class.forName(className);

        // 获取当前类中的所有属性
        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            String paramName = field.getName();
            String paramType = field.getGenericType().toString();
            // 根据属性的类型设置基类值 通过paramName从request中获取值
            Object value = request.getParameter(paramName);
            if (value == null) {
                throw new ParamLackException("请求参数不全");
            }
            ReflectionUtils.setFieldValue(field, object, value);
        }

        return object;
    }

}
