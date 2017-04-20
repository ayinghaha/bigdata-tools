package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;

/**
 * Created by jdshao on 2017/4/17
 */
public interface UserDao {

    /**
     * 新建客户 用户组
     * @param group     客户对象
     * @return  生成主键id
     */
    int createUserGroup(Group group);

    /**
     * 用户注册
     * @param user 注册保存对象
     * @return  生成主键id
     */
    int userRegist(User user);

}
