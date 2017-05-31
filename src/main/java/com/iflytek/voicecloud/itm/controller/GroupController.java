package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.GroupDto;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.service.GroupService;
import com.iflytek.voicecloud.itm.utils.HttpUtil;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.locks.Condition;

/**
 * 客户(用户组)
 */
@Controller
@RequestMapping("/group")
public class GroupController  {

    @Autowired
    GroupService groupService;

    /**
     * 远程接口url
     */
    private String RPCUrl = "http://zeus.xfyun.cn/insight/acl";

    /**
     * 每页用户组条数
     */
    private static int perPage = 5;

    @RequestMapping("/add")
    public void addGroup(HttpServletRequest request, HttpServletResponse response, Group group) throws Exception {

        Message message = new Message(-1, "");
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

        // 调用远程接口获取token
        Map<String, String> param = new HashMap<String, String>();
        param.put("user", group.getName());
        param.put("passwd", "");
        param.put("operation", "regist");
        String RPCResult = HttpUtil.getRPCResponse(RPCUrl, param);
        Map<String, Object> resObj = new HashMap<String, Object>();
        try {
            resObj = JsonUtil.JsonToMap(RPCResult);
            if ((Integer)resObj.get("ret") != 0) {
                ResponseUtil.setResponseJson(response, new Message(-1, "远程接口错误:" + resObj.get("ret")));
            }
        } catch (Exception e) {
            ResponseUtil.setResponseJson(response, new Message(-1, "远程接口错误:" + resObj.get("ret")));
        }

        try {
            group.setToken((String) resObj.get("data"));
            groupService.addGroup(group);
            message.setState(1);
            message.setData("添加成功");
        } catch (Exception e) {
            message.setData("名称或企业名重复");
        } finally {
            ResponseUtil.setResponseJson(response, message);
        }
    }

    @RequestMapping("/get")
    public void getGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        String name = request.getParameter("name") != null ? request.getParameter("name") : "";

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("page", (page - 1) * perPage);
        condition.put("name", name);
        condition.put("perPage", perPage);
        List<Group> groups = groupService.getGroup(condition);
        List<Map<String, Object>> groupJson = new ArrayList<Map<String, Object>>();
        for (Group group : groups) {
            List<User> users = groupService.getUserListByGroup(group.getId());
            group.setUsers(users);
            groupJson.add(GroupDto.formatGroupJson(group));
        }

        // 获取group总数
        int total = groupService.getGroupCount(condition);
        int totalPage = (total % perPage) == 0 ? total / perPage : total / perPage + 1;
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("groupList", groupJson);
        resMap.put("totalPage", totalPage);
        resMap.put("perPage", perPage);
        resMap.put("total", total);

        Message message = new Message();
        message.setState(1);
        message.setData(resMap);

        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/update")
    public void updateGroup(HttpServletRequest request, HttpServletResponse response, Group group) throws Exception {

        Message message = new Message(-1, "");
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

    @RequestMapping("/delete")
    public void deleteGroup(HttpServletRequest request, HttpServletResponse response, Group group) throws Exception {

        Message message = new Message(-1, "");
        if (group.getId() == 0) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测客户是否存在
        Group detectGroup = groupService.getGroupById(group.getId());
        if (detectGroup == null) {
            message.setData("删除客户不存在");
            ResponseUtil.setResponseJson(response, message);
            return;
        }

        // 删除客户 user_group_link 为casecade 会一并删除
        if (groupService.deleteGroupById(detectGroup.getId()) > 0) {
            message.setState(1);
            message.setData("删除成功");
        } else {
            message.setData("删除失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }




}
