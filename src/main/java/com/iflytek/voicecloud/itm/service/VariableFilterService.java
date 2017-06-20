package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.config.VariableFilter;

import java.util.List;

/**
 * Created by jdshao on 2017/3/6
 */
public interface VariableFilterService {

    int addVariableFileter(VariableFilter filter);

    VariableFilter getVariableFilterById(int id);

    List<VariableFilter> getFilterByTriggerId(int TriggerId);

    List<VariableFilter> getFilterByIdList(List filterIds);

    List<VariableFilter> getFilterByVariableId(int variableId);

    int addFilterList(List<VariableFilter> variableFilters);

    int deleteFilterByTrigger(int triggerId);
}
