package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/19
 */

public interface GroupService {

    int addGroup(Group group);

    int updateGroup(Group group);

    List<Group> getGroup(Map<String, Object> condition);

    Group getGroupById(int groupId);

    List<User> getUserListByGroup(int groupId);

    int getGroupCount(Map<String, Object> condition);

    int deleteGroupById(int groupId);
}
