package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.config.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户数据返回格式
 */
public class UserDto {

    public static Map<String, Object> formatUserJson(User user) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("id", user.getId());
        resMap.put("userName", user.getUserName());
        resMap.put("remark", user.getRemark());

        return resMap;
    }


}
