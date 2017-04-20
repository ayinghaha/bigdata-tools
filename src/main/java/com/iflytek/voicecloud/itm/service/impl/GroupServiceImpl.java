package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.GroupDao;
import com.iflytek.voicecloud.itm.entity.Group;
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
}
