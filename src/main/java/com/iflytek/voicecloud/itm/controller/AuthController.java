package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.GroupDto;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.service.GroupService;
import com.iflytek.voicecloud.itm.service.UserService;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import com.iflytek.voicecloud.itm.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户登录
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @RequestMapping("/login")
    public void userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message(-1, "");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        if (userName == null || password == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // TODO admin 用户特殊处理 检测IP是否满足条件

        // 查询用户
        User user = userService.getUserByName(userName);
        if (user == null) {
            message.setData("用户名不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 验证密码是否正确
        String detectPassword = StringUtil.generateMd5(password + user.getSalt());
        if (!detectPassword.equals(user.getPassword())) {
            message.setData("用户密码不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 登录状态保存到session中
        request.getSession().setAttribute("userName", userName);
        List<Group> groups = userService.getGroupListByUser(user);
        List<Map<String, Object>> groupResList = new ArrayList<Map<String, Object>>();
        if (groups.size() > 0) {
            for (Group group : groups) {
                groupResList.add(GroupDto.formatGroupJson(group));
            }
        }
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("groupList", groupResList);

        message.setState(1);
        message.setData(resMap);
        ResponseUtil.setResponseJson(response, message);
    }
}
