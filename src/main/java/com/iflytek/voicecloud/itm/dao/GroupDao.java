package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;

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

    /**
     * 通过主键查询客户信息
     * @param groupId   group主键
     * @return      group对象
     */
    Group getGroupById(int groupId);

    /**
     * 获取客户下的用户
     * @param id     客户对象id
     * @return     用户列表
     */
    List<User> getUserListByGroup(int id);

    /**
     * 获取用户组总数
     * @param condition     查询条件
     * @return      用户组总数
     */
    int getGroupCount(Map<String, Object> condition);

    /**
     * 根据客户id删除客户
     * @param groupId       客户id
     * @return      是否成功
     */
    int deleteGroupById(int groupId);

    /**
     * 根据Container名称以及userid模糊查询得到关联的group并分页数据
     * @param condition     查询条件
     * @return  客户列表
     */
    List<Group> getContainerConditionGroup(Map<String, Object> condition);

}
