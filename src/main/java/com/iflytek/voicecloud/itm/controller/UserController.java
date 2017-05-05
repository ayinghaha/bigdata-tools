package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.Privilege;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.entity.UserGroupLink;
import com.iflytek.voicecloud.itm.service.GroupService;
import com.iflytek.voicecloud.itm.service.UserService;
import com.iflytek.voicecloud.itm.utils.HttpUtil;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import com.iflytek.voicecloud.itm.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * Created by jdshao on 2017/4/20
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    /**
     * 远程接口url
     */
    private String RPCUrl = "http://zeus.xfyun.cn/insight/acl";

    @RequestMapping("/add")
    public void addUser(HttpServletRequest request, HttpServletResponse response, User user) throws Exception{

        Message message = new Message(-1, "");
        String userGroupId = request.getParameter("userGroupId");
        if (user.getUserName() == null || user.getRemark() == null || userGroupId == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测客户是否存在
        Group group = groupService.getGroupById(Integer.parseInt(userGroupId));
        if (group == null) {
            message.setData("绑定客户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 获取客户下的所有用户 检测当前用户名是否存在
        List<User> users = groupService.getUserListByGroup(group.getId());
        List<String> userNames = new ArrayList<String>();
        for (User tmpUser : users) {
            userNames.add(tmpUser.getUserName());
        }
        if (userNames.contains(user.getUserName())) {
            message.setData("此客户已绑定当前用户");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 查询当前用户名是否存在，存在则建立关系，不存在则新建
        User detectUser = userService.getUserByName(user.getUserName());
        if (detectUser == null) {
            Message getMsg = registUser(user, message);
            if (getMsg.getState() != 1) {
                ResponseUtil.setResponseJson(response, message);
                return ;
            }
        } else {
            // 对象赋值
            user = detectUser;
        }

        // 建立客户和用户联系
        UserGroupLink userGroupLink = new UserGroupLink();
        userGroupLink.setUser(user);
        userGroupLink.setGroup(group);
        userGroupLink.setRemark(user.getRemark());
        try {
            userService.addUserGroupLink(userGroupLink);
            message.setState(1);
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("userName", user.getUserName());
            resMap.put("password", user.getPlainPassword());
            message.setData(resMap);
        } catch (Exception e) {
            message.setData("此客户已绑定当前用户");
        }

        ResponseUtil.setResponseJson(response, message);
    }

    /**
     * 内部调用，添加用户
     * @param user      添加对象
     * @return      添加对象
     */
    private Message registUser(User user, Message message) throws Exception {
        // 生成密码和密码盐
        String uuid = UUID.randomUUID().toString();
        String plainPassword = uuid.substring(uuid.length()-8, uuid.length()).toLowerCase();
        String salt = StringUtil.generateSalt();
        String password = StringUtil.generateMd5(plainPassword+salt);

        // 调用远程接口获取token
        Map<String, String> param = new HashMap<String, String>();
        param.put("user", user.getUserName());
        param.put("passwd", password);
        param.put("operation", "regist");
        String RPCResult = HttpUtil.getRPCResponse(RPCUrl, param);
        Map<String, Object> resObj = new HashMap<String, Object>();
        try {
            resObj = JsonUtil.JsonToMap(RPCResult);
            // TODO test
            resObj.put("ret", 0);resObj.put("token", "123456");
            if ((Integer)resObj.get("ret") != 0) {
                message.setData("远程接口错误:" + resObj.get("ret"));
                return message;
            }
        } catch (Exception e) {
            resObj.put("ret", 0);resObj.put("token", "123456");
            System.out.println("远程接口有问题");
        }

        // 填充对象
        user.setNickName(user.getUserName());
        user.setPassword(password);
        user.setPlainPassword(plainPassword);
        user.setPassword(password);
        user.setSalt(salt);
        user.setToken((String) resObj.get("token"));
        user.setRegistTime(new Date());
        // 保存用户
        try {
            userService.userRegist(user);
            message.setState(1);
            HashMap<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("userName", user.getUserName());
            resMap.put("password", user.getPlainPassword());
            message.setData(resMap);
        } catch (Exception e) {
            e.printStackTrace();
            message.setData("添加用户失败");
        }
        return message;
    }

    @RequestMapping("/resetPassword")
    public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message(-1, "");
        String userId = request.getParameter("userId");
        if (userId == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        User user = userService.getUserById(Integer.parseInt(userId));
        if (user == null) {
            message.setData("用户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 生成新的密码并更新用户
        String uuid = UUID.randomUUID().toString();
        String plainPassword = uuid.substring(uuid.length()-8, uuid.length()).toLowerCase();
        String password = StringUtil.generateMd5(plainPassword + user.getSalt());
        user.setPlainPassword(plainPassword);
        user.setPassword(password);

        if(userService.UpdateByUser(user) > 0) {
            message.setState(1);
            Map<String, String> resMap = new HashMap<String, String>();
            resMap.put("userName", user.getUserName());
            resMap.put("password", user.getPlainPassword());
            message.setData(resMap);
        } else {
            message.setData("更新失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/setUserRemark")
    public void setUserRemark(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message(-1, "");
        String groupId = request.getParameter("groupId");
        String userId = request.getParameter("userId");
        String remark = request.getParameter("remark");
        if (groupId == null || userId == null || remark == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        Group group = groupService.getGroupById(Integer.parseInt(groupId));
        if (group == null) {
            message.setData("客户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }
        User user = userService.getUserById(Integer.parseInt(userId));
        if (user == null) {
            message.setData("用户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 直接更新链接对象
        UserGroupLink userGroupLink = new UserGroupLink();
        userGroupLink.setUser(user);
        userGroupLink.setGroup(group);
        userGroupLink.setRemark(remark);
        if (userService.updateUserGroupLink(userGroupLink) > 0) {
            message.setState(1);
            message.setData("更新成功");
        } else {
            message.setData("更新失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/delete")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message(-1, "");
        String groupId = request.getParameter("groupId");
        String userId = request.getParameter("userId");
        if (groupId == null || userId == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        Group group = groupService.getGroupById(Integer.parseInt(groupId));
        if (group == null) {
            message.setData("删除用户所属客户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }
        User user = userService.getUserById(Integer.parseInt(userId));
        if (user == null) {
            message.setData("删除用户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 删除用户连接对象
        UserGroupLink userGroupLink = new UserGroupLink();
        userGroupLink.setGroup(group);
        userGroupLink.setUser(user);
        userService.deleteUserGroupLink(userGroupLink);
        message.setState(1);
        message.setData("删除成功");
        ResponseUtil.setResponseJson(response, message);
    }

}
