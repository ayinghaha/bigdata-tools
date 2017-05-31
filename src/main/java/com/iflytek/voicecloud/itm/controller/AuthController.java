package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.GroupDto;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.Privilege;
import com.iflytek.voicecloud.itm.entity.User;
import com.iflytek.voicecloud.itm.entity.UserGroupLink;
import com.iflytek.voicecloud.itm.service.GroupService;
import com.iflytek.voicecloud.itm.service.UserService;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import com.iflytek.voicecloud.itm.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

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
    public void userLogin(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) throws Exception {

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

        // 更新用户上次登录时间
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLogin(new Date());
        userService.UpdateByUser(updateUser);

        // 获取所有及客户列表
        List<Group> groups = userService.getGroupListByUser(user);
        List<Map<String, Object>> groupResList = new ArrayList<Map<String, Object>>();
        if (groups.size() > 0) {
            for (Group group : groups) {
                groupResList.add(GroupDto.formatGroupJson(group));
            }
        }
        Map<String, Object> resMap = new HashMap<String, Object>();
        int isAdmin = userName.equals("admin") ? 1 : -1;
        resMap.put("isAdmin", isAdmin);
        resMap.put("userId", user.getId());
        resMap.put("userName", user.getUserName());
        resMap.put("groupList", groupResList);

        // 登录状态保存到session中
        httpSession.setAttribute("userName", userName);
        httpSession.setAttribute("groups", groups);

        message.setState(1);
        message.setData(resMap);
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, User user) throws Exception {

        Message message = new Message(-1, "");
        if (user.getId() == 0 || user.getUserName() == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测用户是否存在
        User detectUser = userService.getUserById(user.getId());
        if (detectUser == null) {
            message.setData("用户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        String userName = (String) httpSession.getAttribute("userName");
        if (userName == null || userName.equals("")) {
            message.setData("用户未登录");
        } else {
            httpSession.setAttribute("userName", "");
            message.setState(1);
            message.setData("用户退出成功");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/getUserPrivileges")
    public void getUserPrivileges(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message(-1, "");
        String userId = request.getParameter("userId");
        String groupId = request.getParameter("groupId");
        if (userId == null || groupId == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("userId", userId);
        condition.put("groupId", groupId);
        UserGroupLink userGroupLink = userService.getUserGroupLink(condition);
        if (userGroupLink == null) {
            message.setData("用户与客户连接不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 获取序列化对象保存的相对路径
        String privilegeObjPath = this.getClass().getClassLoader().getResource("").getPath() + "com/iflytek/voicecloud/itm/config/privilege.obj";
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(privilegeObjPath));
        Privilege root = (Privilege) in.readObject();
        for (int i = 0; i < root.getChildren().length ; i++) {
            Privilege leaf = root.getChildren()[i];
            if (leaf.getChildren().length > 0) {
                // 遍历每个大标签的子标签，通过与运算得到是否有该子标签的权限
                for (int j = 0; j < leaf.getChildren().length; j ++ ) {
                    int value = leaf.getChildren()[j].getValue();
                    if ( (userGroupLink.getPrivilege() & value) > 0) {
                        leaf.getChildren()[j].setChecked(1);
                    }
                }
            } else {
                if ( (leaf.getValue() & userGroupLink.getPrivilege()) > 0 ) {
                    leaf.setChecked(1);
                }
            }
            root.getChildren()[i] = leaf;
        }

        message.setState(1);
        message.setData(root);
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/setUserPrivileges")
    public void setUserPrivileges(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message(-1, "");
        String userId = request.getParameter("userId");
        String groupId = request.getParameter("groupId");
        String privilege = request.getParameter("privilege");
        if (userId == null || groupId == null || privilege == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("userId", userId);
        condition.put("groupId", groupId);
        UserGroupLink userGroupLink = userService.getUserGroupLink(condition);
        if (userGroupLink == null) {
            message.setData("用户与客户连接不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        User user = userService.getUserById(Integer.parseInt(userId));
        Group group = groupService.getGroupById(Integer.parseInt(groupId));
        if (user == null || group == null) {
            message.setData("用户或客户不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        UserGroupLink groupLink = new UserGroupLink();
        groupLink.setUser(user);
        groupLink.setGroup(group);
        groupLink.setPrivilege(Integer.parseInt(privilege));
        if (userService.updateUserGroupLink(groupLink) > 0) {
            message.setState(1);
            message.setData("更新成功");
        } else {
            message.setData("更新失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }
}
