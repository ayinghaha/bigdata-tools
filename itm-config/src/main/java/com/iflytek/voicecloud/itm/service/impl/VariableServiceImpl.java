package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.config.VariableDao;
import com.iflytek.voicecloud.itm.entity.config.variable.Variable;
import com.iflytek.voicecloud.itm.service.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/3/1
 */
@Service
public class VariableServiceImpl implements VariableService {

    @Autowired
    private VariableDao variableDao;

    /**
     * 添加变量
     * @param variableObject 变量子类
     * @return  添加成功返回主键id
     */
    public int saveVariable(Object variableObject) {
        int key = variableDao.addVariable((Variable) variableObject);
        return key;
    }

    /**
     * 根据主键id获取变量对象
     * @param id  主键id
     * @return  变量对象
     */
    public Variable getVariableById(int id) {
        return variableDao.getVariableById(id);
    }

    /**
     * 根据条件查询变量列表
     * @param condition  查询的变量对象
     * @return  对象列表
     */
    public List<Variable> getVariable(Map<String, Object> condition) {
        return variableDao.getVariable(condition);
    }

    /**
     * 通过变量主键删除变量
     * @param variableId    变量主键
     * @return  影响行数
     */
    public int deleteVariable(int variableId) {
        return variableDao.deleteVariableById(variableId);
    }

    /**
     * 通过变量对象更新变量
     * @param variable    变量对象
     * @return 影响行数
     */
    public int updateVariableById(Object variable) {
        return variableDao.updateVariableById(variable);
    }
}
