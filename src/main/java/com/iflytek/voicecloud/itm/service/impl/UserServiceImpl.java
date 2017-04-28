package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.UserDao;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.entity.UserGroupLink;
import com.iflytek.voicecloud.itm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    /**
     * 通过用户主键获取id
     * @param userId    用户主键
     * @return      返回User对象
     */
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    /**
     * 添加用户和客户关联
     * @param userGroupLink     关联对象
     * @return      生成主键
     */
    public int addUserGroupLink(UserGroupLink userGroupLink) {
        return userDao.addUserGroupLink(userGroupLink);
    }

    /**
     * 通过用户名获取用户对象 (用户名唯一)
     * @param name      检测用户名
     * @return      用户对象
     */
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    /**
     * 删除用户和客户连接
     * @param userGroupLink     用户、客户连接对象
     * @return      是否成功
     */
    public int deleteUserGroupLink(UserGroupLink userGroupLink) {
        return userDao.deleteUserGroupLink(userGroupLink);
    }

    /**
     * 更新用户连接对象
     * @param userGroupLink     连接对象
     * @return      是否成功
     */
    public int updateUserGroupLink(UserGroupLink userGroupLink) {
        return userDao.updateUserGroupLink(userGroupLink);
    }

    /**
     * 更新User对象
     * @param user      user对象
     * @return      是否成功
     */
    public int UpdateByUser(User user) {
        if (user.getId() == 0) {
            return -1;
        }
        return userDao.UpdateByUser(user);
    }

    /**
     * 根据查询条件获取用户和客户连接对象
     * @param condition     查询条件
     * @return      连接对象
     */
    public UserGroupLink getUserGroupLink(Map<String, Object> condition) {
        return userDao.getUserGroupLink(condition);
    }

    /**
     * 通过User对象获取客户列表
     * @param user      用户对象
     * @return      group列表
     */
    public List<Group> getGroupListByUser(User user) {
        return userDao.getGroupListByUser(user);
    }
}
