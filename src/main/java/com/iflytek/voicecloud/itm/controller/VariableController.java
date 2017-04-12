package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.Exception.ParamLackException;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.dto.VariableDto;
import com.iflytek.voicecloud.itm.entity.VariableFilter;
import com.iflytek.voicecloud.itm.entity.variable.Variable;
import com.iflytek.voicecloud.itm.service.VariableFilterService;
import com.iflytek.voicecloud.itm.service.VariableService;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ReflectionUtils;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 变量管理controller
 */
@Controller
@RequestMapping("/variable")
public class VariableController {

    @Autowired
    VariableService variableService;

    @Autowired
    VariableFilterService filterService;

    /**
     * 全部变量类型数组
     */
    private static final String[] variableTypeList = {"Cookie", "Debug", "CustomJS", "DataLayerVar", "DOMElement", "JSVar", "RandomNum", "ConstantStr"};

    /**
     * 反射对象路径
     */
    private static final String reflectPath = "com.iflytek.voicecloud.itm.entity.variable.";

    @RequestMapping("/add")
    public void addVariable(HttpServletRequest request, HttpServletResponse response, Variable variable) throws Exception{

        // 返回对象
        Message message = new Message(-1, "");

        // 反射得到子类变量对象
        List<String> asList = Arrays.asList(variableTypeList);
        if (variable.getItmID() == null || variable.getContainerID() == null || variable.getName() == null || variable.getType() == null || !asList.contains(variable.getType())) {
            message.setData("参数不全或参数类型不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测是否存在重名变量
        if (detectVariableName(variable)) {
            message.setData("相同Container下添加变量name相同");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 抓取参数为空的异常，返回异常信息
        try {
            Object subVariable = reflectLoadSubVariable(variable, request);
            if (variableService.saveVariable(subVariable) > 0) {
                message.setState(1);
                message.setData("插入成功");
            } else {
                message.setData("插入失败");
            }
        } catch (Exception lackParamException) {
            message.setData(lackParamException.getMessage());
        } finally {
            ResponseUtil.setResponseJson(response, message);
        }
    }

    @RequestMapping("/get")
    public void getVariable(HttpServletRequest request, HttpServletResponse response, Variable variable) throws Exception {

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("id", variable.getId());
        condition.put("itmID", variable.getItmID());
        condition.put("containerID", variable.getContainerID());
        condition.put("name", variable.getName());
        condition.put("type", variable.getType());
        List<Variable> variables = variableService.getVariable(condition);
        Message message = new Message();
        if (variables.size() == 0) {
            message.setState(-1);
            message.setData("查询结果为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        List<Map<String, Object>> varResList = new ArrayList<Map<String, Object>>();
        for (Variable var : variables) {
            varResList.add(VariableDto.formatVariableJson(var));
        }

        message.setState(1);
        message.setData(varResList);
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/delete")
    public void deleteVariable(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = request.getParameter("id");
        Message message = new Message(-1, "");

        if (id == null) {
            message.setData("删除变量id为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        int variableId = Integer.parseInt(id);
        // 获取要删除的变量
        Variable variable = variableService.getVariableById(variableId);
        if (variable == null) {
            message.setData("要删除的变量不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!variable.getItmID().equals("jdshao")) {
            message.setData("当前登录用户无权删除该变量");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测是否存在过滤器包含当前变量，存在则不可删除
        List<VariableFilter> filters = filterService.getFilterByVariableId(variableId);
        if (filters.size() > 0) {
            message.setData("存在使用该变量的过滤器禁止删除");
        } else {
            variableService.deleteVariable(variableId);
            message.setState(1);
            message.setData("删除成功");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/update")
    public void updateVariable(HttpServletRequest request, HttpServletResponse response, Variable variable) throws Exception {

        int id = variable.getId();

        Message message = new Message();
        message.setState(-1);

        if (id == 0) {
            message.setData("变量id为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测变量自身属性是否存 反射检测字段是否存在
        Object subVariable = null;
        if (variable.getType() != null) {
            try {
                subVariable = reflectLoadSubVariable(variable, request);
            } catch (Exception e) {
                message.setData("类型不正确或该类型变量字段不存在");
                ResponseUtil.setResponseJson(response, message);
                return ;
            }
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("id", variable.getId());
        condition.put("itmID", variable.getItmID());
        condition.put("containerID", variable.getContainerID());
        List<Variable> detectVarList = variableService.getVariable(condition);
        if (detectVarList.size() == 0) {
            message.setData("更新变量不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!detectVarList.get(0).getItmID().equals("jdshao")) {
            message.setData("当前登录用户无权删除该变量");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测是否存在重名变量
        if (detectVariableName(variable)) {
            message.setData("相同Container下添加变量name相同");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        if (variableService.updateVariableById(subVariable) > 0) {
            message.setState(1);
            message.setData("更新成功");
        } else {
            message.setData("更新失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }

    /**
     * 反射并加载属性得到变量子类
     * @param variable  变量对象
     * @param request   请求对象，其中包含变量的各个属性
     * @return          变量子类对象
     * @throws Exception    属性不存在抛出异常
     */
    private Object reflectLoadSubVariable(Variable variable, HttpServletRequest request) throws Exception {
        String className = reflectPath + variable.getType();
        Class variableClass = Class.forName(className);
        // 调用构造方法
        Object variableObject = variableClass.getDeclaredConstructor(int.class, String.class, String.class, String.class, String.class, Date.class, Date.class).
                newInstance(variable.getId(), variable.getItmID(), variable.getContainerID(), variable.getName(), variable.getType(), variable.getRegistTime(), variable.getUpdateTime());
        return ReflectionUtils.loadObject(className, variableObject, request);
    }

    /**
     * 检测变量名是否重复
     * @param variable  变量对象
     */
    private boolean detectVariableName(Variable variable) {
        // 检测是否存在重名变量
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("itmID", variable.getItmID());
        condition.put("containerID", variable.getContainerID());
        condition.put("name", variable.getName());
        List<Variable> detectVar = variableService.getVariable(condition);
        if (detectVar.size() > 0) {
            int detectId = detectVar.get(0).getId();
            if (detectId != variable.getId()) {
                return true;
            }
        }
        return false;
    }

}
