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
    public void addVariable(HttpServletRequest request, HttpServletResponse response) throws Exception{

        // 返回对象
        Message message = new Message();

        // 获取请求参数
        String itmID = request.getParameter("itmID");
        String containerID = request.getParameter("containerID");
        String name = request.getParameter("name");
        String variableType = request.getParameter("type");

        // 反射得到子类变量对象
        List<String> asList = Arrays.asList(variableTypeList);
        if (itmID == null || containerID == null || name == null || variableType == null || !asList.contains(variableType)) {
            message.setState(-1);
            message.setData("参数不全或参数类型不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 反射得到对象
        String className = reflectPath + variableType;
        Class variableClass = Class.forName(className);
        /*// 调用父类构造方法
        Constructor[] constructors = variableClass.getConstructors();
        Object[] construtorParams = {0, itmID, containerID, name, variableType, new Date(), new Date()};
        // 注意在各自varible中的构造函数，第一个的类型必须符合此处要求
        Object variableObject = variableClass.getConstructor(constructors[0].getParameterTypes()).newInstance(construtorParams);*/
        Object variableObject = variableClass.getDeclaredConstructor(int.class, String.class, String.class, String.class, String.class, Date.class, Date.class).
                newInstance(0, itmID, containerID, name, variableType, new Date(), new Date());
        // 抓取参数为空的异常，返回异常信息
        try {
            Object subVariable = ReflectionUtils.loadObject(className, variableObject, request);
            int resKey = variableService.saveVariable(subVariable);
            if (resKey > 0) {
                message.setState(1);
                message.setData("插入成功");
            } else {
                message.setState(-1);
                message.setData("插入失败");
            }
            ResponseUtil.setResponseJson(response, message);
            return ;
        } catch (ParamLackException lackParamException) {
            message.setState(-1);
            message.setData(lackParamException.getMessage());
            ResponseUtil.setResponseJson(response, message);
        }
    }

    @RequestMapping("/get")
    public void getVariable(HttpServletRequest request, HttpServletResponse response, Variable variable) throws Exception {

        List<Variable> variables = variableService.getVariable(variable);
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
        return ;
    }

    @RequestMapping("/delete")
    public void deleteVariable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Message message = new Message();
        message.setState(-1);

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
                String className = reflectPath + variable.getType();
                Class variableClass = Class.forName(className);
                /*// 调用父类构造方法
                Constructor[] constructors = variableClass.getConstructors();
                Object[] construtorParams = {variable.getId(), variable.getItmID(), variable.getContainerID(), variable.getName(), variable.getType(), variable.getRegistTime(), variable.getUpdateTime()};
                // 注意在各自varible中的构造函数，第一个的类型必须符合此处要求
                Object variableObject = variableClass.getConstructor(constructors[0].getParameterTypes()).newInstance(construtorParams);*/
                Object variableObject = variableClass.getDeclaredConstructor(int.class, String.class, String.class, String.class, String.class, Date.class, Date.class).
                        newInstance(variable.getId(), variable.getItmID(), variable.getContainerID(), variable.getName(), variable.getType(), variable.getRegistTime(), variable.getUpdateTime());
                subVariable = ReflectionUtils.loadObject(className, variableObject, request);
            } catch (Exception e) {
                message.setData("类型不正确或该类型变量字段不存在");
                ResponseUtil.setResponseJson(response, message);
                return ;
            }
        }

        Variable detectVar = variableService.getVariableById(id);
        if (detectVar == null) {
            message.setData("更新变量不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!detectVar.getItmID().equals("jdshao")) {
            message.setData("当前登录用户无权删除该变量");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        int res = variableService.updateVariableById(subVariable);
        if (res > 0) {
            message.setState(1);
            message.setData("更新成功");
        } else {
            message.setData("更新失败");
        }
        ResponseUtil.setResponseJson(response, message);
    }

}
