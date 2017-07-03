package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.config.variable.Variable;

import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/3/1
 */
public interface VariableService {

    int saveVariable(Object variable);

    Variable getVariableById(int id);

    List<Variable> getVariable(Map<String, Object> condition);

    int deleteVariable(int variableId);

    int updateVariableById(Object variable);

}
