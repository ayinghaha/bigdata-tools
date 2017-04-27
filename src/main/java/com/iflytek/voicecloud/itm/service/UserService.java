package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.entity.UserGroupLink;

/**
 * Created by jdshao on 2017/4/20
 */
public interface UserService {

    int userRegist(User user);

    User getUserById(int userId);

    int addUserGroupLink(UserGroupLink userGroupLink);

    User getUserByName(String name);

    int deleteUserGroupLink(UserGroupLink userGroupLink);

    int UpdateByUser(User user);

}
