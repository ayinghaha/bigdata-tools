package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.Group;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/19
 */

public interface GroupService {

    int addGroup(Group group);

    int updateGroup(Group group);

    List<Group> getGroup(Map<String, Object> condition);
}
