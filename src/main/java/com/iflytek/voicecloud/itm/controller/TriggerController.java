package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.dto.TriggerDto;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.entity.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.Trigger;
import com.iflytek.voicecloud.itm.entity.VariableFilter;
import com.iflytek.voicecloud.itm.entity.variable.Variable;
import com.iflytek.voicecloud.itm.service.TriggerService;
import com.iflytek.voicecloud.itm.service.VariableFilterService;
import com.iflytek.voicecloud.itm.service.VariableService;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 触发器Controller
 * Created by jdshao on 2017/3/7
 */
@Controller
@RequestMapping("/trigger")
public class TriggerController {

    /**
     * 触发器 Trigger service层
     */
    @Autowired
    private TriggerService triggerService;

    /**
     * 过滤器 filter service层
     */
    @Autowired
    private VariableFilterService variableFilterService;

    /**
     * 变量 service层
     */
    @Autowired
    private VariableService variableService;

    /**
     * 触发器类型
     */
    private static final String[] triggerTypeList = {"domReady", "windowReady", "pageView", "linkClick", "elementClick", "timer", "jsError", "historyChange", "formSubmit"};

    /**
     * 内置变量类型列表
     */
    private static final String[] buildInTypeList = {"ClickID", "ClickElement", "ClickClass", "ClickTarget", "ClickURL", "ClickText","FormElement", "FormClass", "FormID", "FormTarget", "FormURL", "FormText","ErrorMessage", "ErrorURL", "ErrorLine", "ErrorDebug","PageHostname", "PagePath", "PageURL", "PageReferrer"};

    @RequestMapping("/add")
    public void addTrigger(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String itmID = request.getParameter("itmID");
        String containerID = request.getParameter("containerID");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String delay = request.getParameter("delay");

        Message message = new Message();
        message.setState(-1);

        List<String> asTypeList = Arrays.asList(triggerTypeList);
        if (itmID == null || containerID == null || name == null || type == null || !asTypeList.contains(type)) {
            message.setData("参数不全或参数类型不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (type.equals("timer") && delay == null) {
            message.setData("type为timer时delay参数不能为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 新建trigger
        Trigger trigger = new Trigger(itmID, containerID, name, type, delay, new Date(), new Date());
        int resKey = triggerService.addTrigger(trigger);
        if (resKey > 0) {
            message.setState(1);
            message.setData("添加成功");
        } else {
            message.setData("添加失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    /**
     * 合并添加 添加Trigger同时绑定filter
     * @param request   request对象
     * @param response  response对象
     * @param trigger   接收变量
     * @throws Exception    异常
     */
    @RequestMapping("combineAddUpdate")
    public void addTriggerWithFilter(HttpServletRequest request, HttpServletResponse response, Trigger trigger) throws Exception {

        List<String> asTypeList = Arrays.asList(triggerTypeList);
        if (trigger.getItmID() == null || trigger.getContainerID() == null || trigger.getName() == null || trigger.getType() == null || !asTypeList.contains(trigger.getType())) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全或参数类型不正确"));
            return ;
        } else if (trigger.getType().equals("timer") && trigger.getDelay() == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "type为timer时delay参数不能为空"));
            return ;
        }

        // 当绑定filter不为空时，检测绑定filter合法性
        String filterList = request.getParameter("filterList");
        List<VariableFilter> variableFilters = new ArrayList<VariableFilter>();
        if (filterList != null) {
            List<Map<String, Object>> filtersInfo = JsonUtil.JsonToFilterList(filterList);
            List<String> conditionTypes = Arrays.asList(FilterController.conditionTypesList);
            List<String> buildInTypes = Arrays.asList(buildInTypeList);
            // filterInfo {"condition":"equals","value":"123","variableId":123,"buildInType":"ClickElement"}
            for (Map<String, Object> filterInfo : filtersInfo) {
                // filter 可以添加自定义变量或内置变量, 不能同时为空，自定义变量条件优先
                int variableId = (Integer) filterInfo.get("variableId");
                String buildInType = (String) filterInfo.get("buildInType");
                if (variableId == 0 && buildInType == null) {
                    ResponseUtil.setResponseJson(response, new Message(-1, "自定变量和内置变量同时为空"));
                    return;
                }

                Variable variable = variableId > 0 ? variableService.getVariableById(variableId) : null;
                if (variable != null && !variable.getItmID().equals(trigger.getItmID())) {
                    ResponseUtil.setResponseJson(response, new Message(-1, "绑定变量不存在或无权添加"));
                    return;
                } else if (buildInType != null && !buildInTypes.contains(buildInType)) {
                    ResponseUtil.setResponseJson(response, new Message(-1, "内置变量类型不正确"));
                    return;
                }

                // 生成对象列表
                String condition = (String) filterInfo.get("condition");
                if (!conditionTypes.contains(condition)) {
                    ResponseUtil.setResponseJson(response, new Message(-1, "绑定过滤器类型不正确"));
                    return;
                }
                String value = (String) filterInfo.get("value");
                variableFilters.add(new VariableFilter(trigger.getItmID(), trigger.getContainerID(), "", variable, condition, value, buildInType, trigger, new Date(), new Date() ));
            }
        }

        // 检测 trigger 是否重名
        if (detectTriggerName(trigger)) {
            ResponseUtil.setResponseJson(response, new Message(-1, "相同Container下触发器name相同"));
            return;
        }

        // trigger id 不为空为更新的情况
        int resKey ;
        Message message = new Message(-1, "");
        if (trigger.getId() > 0) {
            resKey = triggerService.updateTrigger(trigger);
            // 将原trigger下的filter全部删除
            variableFilterService.deleteFilterByTrigger(trigger.getId());
            message.setData("更新成功");
        } else {
            // 新建trigger
            trigger.setRegistTime(new Date());
            trigger.setUpdateTime(new Date());
            resKey = triggerService.addTrigger(trigger);
            message.setData("添加成功");
        }

        if (resKey > 0) {
            // 绑定filter不为空时批量添加filter
            if (variableFilters.size() > 0) {
                // 将trigger设置进去
                for (int i = 0; i < variableFilters.size(); i ++) {
                    variableFilters.get(i).setTrigger(trigger);
                }
                variableFilterService.addFilterList(variableFilters);
            }
            message.setState(1);
        } else {
            message.setData("添加失败");
        }

        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/get")
    public void getTrigger(HttpServletRequest request, HttpServletResponse response, Trigger trigger) throws Exception {

        // 获取trigger列表
        List<Trigger> triggers = triggerService.getTriggers(trigger);
        List<Map<String, Object>> triggerResList = new ArrayList<Map<String, Object>>();
        for (Trigger trg : triggers) {
            triggerResList.add(TriggerDto.formatTriggerJson(trg));
        }

        Message message = new Message();
        message.setState(1);
        message.setData(triggerResList);
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/delete")
    public void deleteTrigger(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message();
        message.setState(-1);

        String id = request.getParameter("id");
        if (id == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        int triggerId = Integer.parseInt(id);
        Trigger trigger = triggerService.getTriggerById(triggerId);

        // 获取用户所有的itmID
        List<Group> groupList = (List<Group>) request.getSession().getAttribute("groups");
        List<String> itmIdList = new ArrayList<String>();
        for (Group group : groupList) {
            itmIdList.add(group.getItmID());
        }

        if (trigger == null) {
            message.setData("要删除的触发器不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!itmIdList.contains(trigger.getItmID())) {
            message.setData("当前用户没有删除该触发器权限");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测tag结合
        List<TagTriggerLink> links = triggerService.getLinkByTriggerId(triggerId);
        if (links.size() >= 1) {
            message.setData("存在标签与此触发器结合不能删除");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 删除操作
        int delRes = triggerService.deleteByTriggerId(triggerId);
        if (delRes > 0) {
            message.setState(1);
            message.setData("删除成功");
        } else {
            message.setData("删除失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/update")
    public void updateTrigger(HttpServletRequest request, HttpServletResponse response, Trigger trigger) throws Exception {

        Message message = new Message(-1, "");

        if (trigger.getId() == 0 || trigger.getName() == null || trigger.getType() == null) {
            message.setData("触发器id为空或参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (trigger.getType().equals("timer") && trigger.getDelay() == null) {
            message.setData("type为timer时delay参数不能为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测触发器类型
        List<String> asTypeList = Arrays.asList(triggerTypeList);
        if (!asTypeList.contains(trigger.getType())) {
            message.setData("触发器类型不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测 trigger 是否重名
        if (detectTriggerName(trigger)) {
            message.setData("相同Container下触发器name相同");
            ResponseUtil.setResponseJson(response, message);
            return;
        }

        // 检测trigger是否存在
        Trigger detectTrigger = triggerService.getTriggerById(trigger.getId());
        // 获取用户所有的itmID
        List<Group> groupList = (List<Group>) request.getSession().getAttribute("groups");
        List<String> itmIdList = new ArrayList<String>();
        for (Group group : groupList) {
            itmIdList.add(group.getItmID());
        }

        if (detectTrigger == null) {
            message.setData("要更新的触发器不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!itmIdList.contains(detectTrigger.getItmID())) {
            message.setData("当前用户没有删除该触发器权限");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 更新trigger
        int updateRes = triggerService.updateTrigger(trigger);
        if (updateRes > 0) {
            message.setState(1);
            message.setData("更新成功");
        } else {
            message.setData("更新失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    /**
     * 检测 Trigger 是否重名
     * @param trigger   trigger对象
     * @return  true 重名 false 不重名
     */
    private boolean detectTriggerName(Trigger trigger) {
        Trigger detectTrigger = new Trigger(trigger.getItmID(), trigger.getContainerID(), trigger.getName(), null, null, null, null);
        List<Trigger> detectTriggerList = triggerService.getTriggers(detectTrigger);
        if (detectTriggerList.size() > 0) {
            int detectId = detectTriggerList.get(0).getId();
            if (detectId != trigger.getId()) {
                return true;
            }
        }
        return false;
    }
}
