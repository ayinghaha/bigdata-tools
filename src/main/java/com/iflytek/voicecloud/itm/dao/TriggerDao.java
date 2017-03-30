package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.Tag;
import com.iflytek.voicecloud.itm.entity.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.Trigger;
import com.iflytek.voicecloud.itm.entity.VariableFilter;

import java.util.List;

/**
 * Created by jdshao on 2017/3/7
 */
public interface TriggerDao {

    /**
     * 添加触发器
     * @param trigger 触发器对象
     * @return  添加成功后主键id
     */
    int addTrigger(Trigger trigger);

    /**
     * 根据主键id查询trigger
     * @param id  主键id
     * @return  trigger
     */
    Trigger getTriggerById(int id);

    /**
     * 根据主键列表查询Trigger列表
     * @param triggerIds  主键列表
     * @return  trigger列表
     */
    List<Trigger> getTriggerByIdList(List triggerIds);

    /**
     * 根据trigger中的字段查询trigger列表
     * @param trigger   trigger对象
     * @return  trigger列表
     */
    List<Trigger> getTriggers(Trigger trigger);


    /**
     * 获取Tag下的trigger列表
     * @param tag
     * @return
     */
    List<Trigger> getTagTriggers(Tag tag);

    /**
     * 获取trigger的filters
     * @param trigger   trigger对象
     * @return  filter列表
     */
    List<VariableFilter> getTriggerFilters(Trigger trigger);

    /**
     * 获取trigger的连接对象
     * @param triggerId trigger的主键id
     * @return  连接对象列表
     */
    List<TagTriggerLink> getLinkByTriggerId(Integer triggerId);

    /**
     * 通过trigger主键删除trigger
     * @param triggerId     主键id
     * @return  影响行数
     */
    int deleteByTriggerId(Integer triggerId);

    /**
     * 更新trigger
     * @param trigger  更新对象
     * @return      影响行数
     */
    int updateTrigger(Trigger trigger);
}
