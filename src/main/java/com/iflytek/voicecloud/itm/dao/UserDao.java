package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.entity.UserGroupLink;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/17
 */
public interface UserDao {

    /**
     * 用户注册
     * @param user      注册用户对象
     * @return      生成主键id
     */
    int userRegist(User user);

    /**
     * 通过用户主键获取id
     * @param userId    用户主键
     * @return      返回User对象
     */
    User getUserById(int userId);

    /**
     * 添加用户和客户关联
     * @param userGroupLink     关联对象
     * @return      生成主键
     */
    int addUserGroupLink(UserGroupLink userGroupLink);

    /**
     * 通过用户名获取用户对象 (用户名唯一)
     * @param name      检测用户名
     * @return      用户对象
     */
    User getUserByName(String name);

    /**
     * 根据查询条件获取用户和客户连接对象
     * @param condition     查询条件
     * @return      连接对象
     */
    UserGroupLink getUserGroupLink(Map<String, Object> condition);

    /**
     * 更新用户连接对象
     * @param userGroupLink     连接对象
     * @return      是否成功
     */
    int updateUserGroupLink(UserGroupLink userGroupLink);

    /**
     * 删除用户和客户连接
     * @param userGroupLink     用户、客户连接对象
     * @return      是否成功
     */
    int deleteUserGroupLink(UserGroupLink userGroupLink);

    /**
     * 更新User对象
     * @param user      user对象
     * @return      是否成功
     */
    int UpdateByUser(User user);

    /**
     * 通过User对象获取客户列表
     * @param user      用户对象
     * @return      group列表
     */
    List<Group> getGroupListByUser(User user);

}
