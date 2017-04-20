package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.service.GroupService;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 客户(用户组)
 */
@Controller
@RequestMapping("/group")
public class GroupController  {

    @Autowired
    GroupService groupService;

    @RequestMapping("/add")
    public void addGroup(HttpServletRequest request, HttpServletResponse response, Group group) throws Exception {

        Message message = new Message(-1, "");
        // TODO 检测admin登录未登录禁止操作


        if (group.getName() == null || group.getCompany() == null || group.getRemark() == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 生成itm_id和添加记录时间
        String itmID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        group.setItmID(itmID);
        group.setRegistTime(new Date());
        group.setUpdateTime(new Date());

        try {
            groupService.addGroup(group);
            message.setState(1);
            message.setData("添加成功");
        } catch (Exception e) {
            message.setData("名称或企业名重复");
        } finally {
            ResponseUtil.setResponseJson(response, message);
        }
    }

    @RequestMapping("/update")
    public void updateGroup(HttpServletRequest request, HttpServletResponse response, Group group) throws Exception {

        Message message = new Message(-1, "");
        // TODO 检测admin登录未登录禁止操作


        if (group.getId() == 0 || group.getName() == null || group.getCompany() == null || group.getRemark() == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测客户是否存在
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("id", group.getId());
        List<Group> groups = groupService.getGroup(condition);
        if (groups.size() == 0) {
            message.setData("更新客户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        try {
            groupService.updateGroup(group);
            message.setState(1);
            message.setData("更新成功");
        } catch (Exception e) {
            message.setData("名称或企业名重复");
        } finally {
            ResponseUtil.setResponseJson(response, message);
        }
    }




}
