package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.config.GroupDao;
import com.iflytek.voicecloud.itm.entity.config.Group;
import com.iflytek.voicecloud.itm.entity.config.User;
import com.iflytek.voicecloud.itm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/19
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupDao groupDao;

    /**
     * 添加客户(用户组)
     * @param group     客户对象 用户组对象
     * @return      生成主键id
     */
    public int addGroup(Group group) {
        return groupDao.addGroup(group);
    }

    /**
     * 更新客户信息
     * @param group     客户对象 用户组对象
     * @return      是否成功
     */
    public int updateGroup(Group group) {
        return groupDao.updateGroup(group);
    }

    /**
     * 获取客户列表
     * @param condition     查询条件
     * @return      客户列表
     */
    public List<Group> getGroup(Map<String, Object> condition) {
        return groupDao.getGroup(condition);
    }

    /**
     * 通过主键查询客户信息
     * @param groupId   group主键
     * @return      group对象
     */
    public Group getGroupById(int groupId) {
        return groupDao.getGroupById(groupId);
    }

    /**
     * 获取客户下的用户
     * @param condition     查询条件
     * @return     用户列表
     */
    public List<User> getUserListByGroup(Map<String, Object> condition) {
        return groupDao.getUserListByGroup(condition);
    }

    /**
     * 获取用户组总数
     * @param condition     查询条件
     * @return      用户组总数
     */
    public int getGroupCount(Map<String, Object> condition) {
        return groupDao.getGroupCount(condition);
    }

    /**
     * 根据客户id删除客户
     * @param groupId       客户id
     * @return      是否成功
     */
    public int deleteGroupById(int groupId) {
        return groupDao.deleteGroupById(groupId);
    }

}
