package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.Container;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/6
 */
public class ContainerDto {

    public static Map<String, Object> formatContainerJson(Container container) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        // 设置父类成员变量
        resMap.put("itmID", container.getItmID());
        resMap.put("containerID", container.getContainerID());
        resMap.put("type", container.getType());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        resMap.put("registTime", format.format(container.getRegistTime()));

        return resMap;
    }

}
