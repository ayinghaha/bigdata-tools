package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.VariableFilter;
import com.iflytek.voicecloud.itm.entity.variable.Variable;

import java.util.List;

/**
 * Created by jdshao on 2017/3/6
 */
public interface VariableFilterDao {

    /**
     * 添加变量过滤器
     * @param filter  过滤器对象
     * @return  插入成功返回主键id
     */
    int addVariableFileter(VariableFilter filter);

    /**
     * 根据主键id查询过滤器
     * @param id    主键id
     * @return  过滤器对象
     */
    VariableFilter getVariableFilterById(int id);

    /**
     * 根据trigger主键查询Filter列表
     * @param TriggerId trigger主键
     * @return  filter列表
     */
    List<VariableFilter> getFilterByTriggerId(int TriggerId);

    /**
     * 根据filter主键数组获取列表
     * @param filterIds    filter主键数组
     * @return  过滤器列表
     */
    List<VariableFilter> getFilterByIdList(List filterIds);

    /**
     * 通过变量id获取过滤器列表
     * @param variableId    变量id
     * @return  过滤器列表
     */
    List<VariableFilter> getFilterByVariableId(int variableId);

    /**
     * 添加过滤器列表
     * @param variableFilters  variableFilter 列表
     * @return  int主键
     */
    int addFilterList(List<VariableFilter> variableFilters);

    /**
     * 通过trigger删除fitler
     * @param triggerId triggerid
     * @return 删除结果
     */
    int deleteFilterByTrigger(int triggerId);
}
