package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.dao.VariableFilterDao;
import com.iflytek.voicecloud.itm.entity.VariableFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jdshao on 2017/3/6
 */
@Service
public class VariableFilterServiceImpl implements VariableFilterService {

    @Autowired
    private VariableFilterDao filterDao;

    /**
     * 添加变量过滤器
     * @param filter  过滤器对象
     * @return  插入成功返回主键id
     */
    public int addVariableFileter(VariableFilter filter) {
        return filterDao.addVariableFileter(filter);
    }

    /**
     * 根据主键id查询过滤器
     * @param id    主键id
     * @return  过滤器对象
     */
    public VariableFilter getVariableFilterById(int id) {
        return filterDao.getVariableFilterById(id);
    }

    /**
     * 根据trigger主键查询Filter列表
     * @param triggerId trigger主键
     * @return  filter列表
     */
    public List<VariableFilter> getFilterByTriggerId(int triggerId) {
        return filterDao.getFilterByTriggerId(triggerId);
    }

    /**
     * 根据filter主键数组获取列表
     * @param filterIds    filter主键数组
     * @return  过滤器列表
     */
    public List<VariableFilter> getFilterByIdList(List filterIds) {
        return filterDao.getFilterByIdList(filterIds);
    }

    /**
     * 通过变量id获取过滤器列表
     * @param variableId    变量id
     * @return  过滤器列表
     */
    public List<VariableFilter> getFilterByVariableId(int variableId) {
        return filterDao.getFilterByVariableId(variableId);
    }

    /**
     * 添加过滤器列表
     * @param variableFilters  variableFilter 列表
     * @return  int主键
     */
    public int addFilterList(List<VariableFilter> variableFilters) {
        return filterDao.addFilterList(variableFilters);
    }

    /**
     * 通过trigger删除其下的所有filter
     * @param triggerId Triggerid
     * @return  影响行数
     */
    public int deleteFilterByTrigger(int triggerId) {
        return filterDao.deleteFilterByTrigger(triggerId);
    }
}
