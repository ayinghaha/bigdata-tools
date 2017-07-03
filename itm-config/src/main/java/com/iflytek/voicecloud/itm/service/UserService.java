package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.config.Group;
import com.iflytek.voicecloud.itm.entity.config.User;
import com.iflytek.voicecloud.itm.entity.config.UserGroupLink;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/20
 */
public interface UserService {

    int userRegist(User user);

    User getUserById(int userId);

    int addUserGroupLink(UserGroupLink userGroupLink);

    User getUserByName(String name);

    int deleteUserGroupLink(UserGroupLink userGroupLink);

    int updateUserGroupLink(UserGroupLink userGroupLink);

    int UpdateByUser(User user);

    UserGroupLink getUserGroupLink(Map<String, Object> condition);

    List<Group> getGroupListByUser(User user);

    int countUserGroupLink(Map<String, Object> condition);

    int deleteUser(User user);

}
