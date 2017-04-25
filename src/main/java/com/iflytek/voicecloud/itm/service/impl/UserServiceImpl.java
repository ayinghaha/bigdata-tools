package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.UserDao;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.entity.UserGroupLink;
import com.iflytek.voicecloud.itm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jdshao on 2017/4/20
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    /**
     * 用户注册
     * @param user      注册用户对象
     * @return      生成主键id
     */
    public int userRegist(User user) {
        return userDao.userRegist(user);
    }

    public int addUserGroupLink(UserGroupLink userGroupLink) {
        return userDao.addUserGroupLink(userGroupLink);
    }

    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }
}
