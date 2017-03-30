package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.variable.Variable;

import java.util.List;

/**
 * Created by jdshao on 2017/3/1
 */
public interface VariableService {

    int saveVariable(Object variable);

    Variable getVariableById(int id);

    List<Variable> getVariable(Variable variable);

    int deleteVariable(int variableId);

    int updateVariableById(Object variable);

}
