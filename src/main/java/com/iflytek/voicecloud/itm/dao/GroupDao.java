package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.Group;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/19
 */
public interface GroupDao {

    /**
     * 添加客户(用户组)
     * @param group     客户对象 用户组对象
     * @return      生成主键id
     */
    int addGroup(Group group);

    /**
     * 更新客户信息
     * @param group     客户对象 用户组对象
     * @return      是否成功
     */
    int updateGroup(Group group);

    /**
     * 查询客户信息
     * @param condition     查询条件
     * @return      客户列表
     */
    List<Group> getGroup(Map<String, Object> condition);

}
