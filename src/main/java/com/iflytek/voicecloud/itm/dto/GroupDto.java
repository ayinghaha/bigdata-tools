package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/24
 */
public class GroupDto {

    public static Map<String, Object> formatGroupJson(Group group) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("id", group.getId());
        resMap.put("name", group.getName());
        resMap.put("itmID", group.getItmID());

        List<User> users = group.getUsers();
        List<Map<String, Object>> userMaps = new ArrayList<Map<String, Object>>();
        for (User user : users) {
            userMaps.add(UserDto.formatUserJson(user));
        }
        resMap.put("userList", userMaps);

        return resMap;
    }

}
