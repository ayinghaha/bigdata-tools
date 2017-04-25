package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
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

        // TODO　检测admin用户是否登录

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
            user.setId(detectUser.getId());
        }

        // 建立客户和用户联系
        UserGroupLink userGroupLink = new UserGroupLink();
        userGroupLink.setUser(user);
        userGroupLink.setGroup(group);
        userGroupLink.setRemark(user.getRemark());
        try {
            userService.addUserGroupLink(userGroupLink);
            message.setState(1);
            message.setData("添加用户成功");
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
        Map<String, Object> resObj = JsonUtil.JsonToMap(RPCResult);
        // TODO test
        resObj.put("ret", 0);resObj.put("token", "123456");
        if ((Integer)resObj.get("ret") != 0) {
            message.setData("远程接口错误:" + resObj.get("ret"));
            return message;
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
            message.setData("添加用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            message.setData("添加用户失败");
        }
        return message;
    }

}
