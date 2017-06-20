package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.ContainerDto;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.config.Container;
import com.iflytek.voicecloud.itm.entity.config.Group;
import com.iflytek.voicecloud.itm.entity.config.User;
import com.iflytek.voicecloud.itm.service.ContainerService;
import com.iflytek.voicecloud.itm.service.GroupService;
import com.iflytek.voicecloud.itm.service.UserService;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by jdshao on 2017/4/6
 */
@Controller
@RequestMapping("/container")
public class ContainerController {

    @Autowired
    ContainerService containerService;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    /**
     * container 类型
     */
    public static final String[] containerType = {"Web网站数据", "Web推广数据", "iOS", "Android"};

    /**
     * 每页显示Container数量
     */
    private int perPage = 5;

    @RequestMapping("/add")
    public void addContainer(HttpServletRequest request, HttpServletResponse response, Container container) throws Exception {

        List<String> typeList = Arrays.asList(containerType);
        if (container.getItmID() == null || container.getType() == null || container.getName() == null ) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        } else if (!typeList.contains(container.getType())) {
            ResponseUtil.setResponseJson(response, new Message(-1, "类型不正确"));
            return ;
        }
        
        // 检测相同itmid下名称和是否重复
        Map<String, Object> condition = new HashMap<String, Object>();
        List<String> itmIdList = new ArrayList<String>();
        itmIdList.add(container.getItmID());
        condition.put("itmID", itmIdList);
        condition.put("name", container.getName());
        List<Container> detectContainer = containerService.getContainerList(condition);
        if (detectContainer.size() > 0) {
            ResponseUtil.setResponseJson(response, new Message(-1, "当前ITM账号下存在同名Container"));
            return ;
        }

        // 添加Container
        container.setRegistTime(new Date());
        container.setUpdateTime(new Date());
        Long registTime = container.getRegistTime().getTime()/1000;
        container.setContainerID(String.valueOf(Long.toHexString(registTime)));
        int resKey = containerService.addContainer(container);
        Message message = new Message();
        if (resKey < 0) {
            message.setState(-1);
            message.setData("添加失败");
        } else {
            message.setState(1);
            message.setData("添加成功");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/get")
    public void getContainer(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String keyWords = request.getParameter("keyWords");
        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        // 组装查询客户条件
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("groupName", keyWords);
        condition.put("page", (page - 1) * this.perPage);
        condition.put("perPage", this.perPage);

        // 检测登录用户，获取用户关联的客户并查询所有container
        String userName = (String) request.getSession().getAttribute("userName");
        // 查询container条件
        Map<String, Object> containerCondition = new HashMap<String, Object>();
        if (userName != null && !userName.equals("admin")) {
            // 获取当前条件下的关联
            User user = userService.getUserByName(userName);
            condition.put("userId", user.getId());
        } else {
            // 分析接口使用，查询某个container下的配置
            String containerID = request.getParameter("containerID");
            containerCondition.put("containerID", containerID);
        }

        // 关联客户
        List<Group> associateGroups = groupService.getGroup(condition);
        // 只取当前页的group作为条件
        List<String> itmIdList = new ArrayList<String>();
        for (Group group : associateGroups) {
            itmIdList.add(group.getItmID());
        }
        // 判断是否为空
        if (itmIdList.size() > 0) {
            containerCondition.put("itmID", itmIdList);
        }

        List<Container> containers = containerService.getContainerList(containerCondition);
        List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
        for (Group group : associateGroups) {
            Map<String, Object> groupMap = new HashMap<String, Object>();
            groupMap.put("groupId", group.getId());
            groupMap.put("groupName", group.getName());
            groupMap.put("itmID", group.getItmID());
            List<Map<String, Object>> containerList = new ArrayList<Map<String, Object>>();
            for (Container container : containers) {
                if (container.getItmID().equals(group.getItmID())) {
                    containerList.add(ContainerDto.formatContainerJson(container));
                }
            }
            groupMap.put("containerList", containerList);

            resMapList.add(groupMap);
        }

        // 总条数
        int total = groupService.getGroupCount(condition);
        // 总页数
        int totalPage = (total % this.perPage) == 0 ? total / this.perPage : total / this.perPage + 1;

        Map<String, Object> resData = new HashMap<String, Object>();
        resData.put("groupList", resMapList);
        resData.put("total", total);
        resData.put("totalPage", totalPage);
        resData.put("perPage", this.perPage);

        ResponseUtil.setResponseJson(response, new Message(1, resData));
    }

    @RequestMapping("/glist")
    public void getContainByGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String itmID = request.getParameter("itmID");
        if (itmID == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return;
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        List<String> itmIdList = new ArrayList<String>();
        itmIdList.add(itmID);
        condition.put("itmID", itmIdList);

        List<Container> containerList = containerService.getContainerList(condition);

        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("containerList", containerList);

        ResponseUtil.setResponseJson(response, new Message(1, resMap));
    }

}
