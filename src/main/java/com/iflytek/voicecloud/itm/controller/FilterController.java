package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.FilterDto;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.config.Trigger;
import com.iflytek.voicecloud.itm.entity.config.VariableFilter;
import com.iflytek.voicecloud.itm.entity.config.variable.Variable;
import com.iflytek.voicecloud.itm.service.TriggerService;
import com.iflytek.voicecloud.itm.service.VariableFilterService;
import com.iflytek.voicecloud.itm.service.VariableService;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 过滤器controller
 */
@Controller
@RequestMapping("/filter")
public class FilterController {

    /**
     * trigger service层
     */
    @Autowired
    TriggerService triggerService;

    /**
     * 过滤器service层
     */
    @Autowired
    VariableFilterService filterService;

    /**
     * 变量service层
     */
    @Autowired
    VariableService variableService;

    public static final String[] conditionTypesList = {"equals","contains","matchesCSS","matchesRegEx","notEquals","notContains","notMatchesCSS","notMatchesRegEx","lt","lte","gt","gte"};

    @RequestMapping("/add")
    public void addVariableFilter(HttpServletRequest request, HttpServletResponse response, VariableFilter variableFilter) throws Exception {

        String variableId = request.getParameter("variableId");
        String triggerId = request.getParameter("triggerId");
        Message message = new Message(-1, "");
        List<String> conditionTypes = Arrays.asList(conditionTypesList);
        if (variableFilter.getItmID() == null || variableFilter.getContainerID() == null || variableFilter.getCondition() == null || variableFilter.getValue() == null || variableId == null || triggerId == null || !conditionTypes.contains(variableFilter.getCondition())) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测变量对象是否存在
        Variable variable = variableService.getVariableById(Integer.parseInt(variableId));
        if (variable == null) {
            message.setData("绑定变量不存在");
            ResponseUtil.setResponseJson(response, message);
            return;
        }
        // 检测绑定触发器是否存在
        Trigger trigger = triggerService.getTriggerById(Integer.parseInt(triggerId));
        if (trigger == null) {
            message.setData("绑定触发器不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 添加过滤器
        variableFilter.setTrigger(trigger);
        variableFilter.setVariable(variable);
        int resKey = filterService.addVariableFileter(variableFilter);
        if (resKey > 0) {
            message.setState(1);
            message.setData("添加成功");
            ResponseUtil.setResponseJson(response, message);
        } else {
            message.setData("添加失败");
            ResponseUtil.setResponseJson(response, message);
        }
    }

    @RequestMapping("/get")
    public void getVariableFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String triggerId = request.getParameter("triggerId");
        Message message = new Message();
        message.setState(-1);
        if (triggerId == null) {
            message.setData("参数不全");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 根据trigger查询绑定的filter
        List<VariableFilter> variableFilters = filterService.getFilterByTriggerId(Integer.parseInt(triggerId));
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        for (VariableFilter variableFilter : variableFilters) {
            resList.add(FilterDto.formatFilterJson(variableFilter));
        }

        // 返回结果
        message.setState(1);
        message.setData(resList);
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/delete")
    public void deleteVariableFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message();
        message.setState(-1);

        String id = request.getParameter("id");
        if (id == null) {
            message.setData("过滤器主键id为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 获取要删除的过滤器
        int filterId = Integer.parseInt(id);
        VariableFilter filter = filterService.getVariableFilterById(filterId);
        if (filter == null) {
            message.setData("要删除的过滤器为不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!filter.getItmID().equals("test")) {
            message.setData("当前用户无权删除该变量");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }
    }
}
