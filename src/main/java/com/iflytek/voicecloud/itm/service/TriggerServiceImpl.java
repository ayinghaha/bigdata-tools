package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.dao.TriggerDao;
import com.iflytek.voicecloud.itm.entity.Tag;
import com.iflytek.voicecloud.itm.entity.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.Trigger;
import com.iflytek.voicecloud.itm.entity.VariableFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jdshao on 2017/3/7
 */
@Service
public class TriggerServiceImpl implements TriggerService {

    @Autowired
    private TriggerDao triggerDao;

    /**
     * 添加触发器
     * @param trigger 触发器对象
     * @return  生成主键
     */
    public int addTrigger(Trigger trigger) {
        return triggerDao.addTrigger(trigger);
    }

    /**
     * 根据主键id查询trigger
     * @param id  主键id
     * @return  trigger
     */
    public Trigger getTriggerById(int id) {
        return triggerDao.getTriggerById(id);
    }

    /**
     * 根据主键列表查询Trigger列表
     * @param triggerIds  主键列表
     * @return  trigger列表
     */
    public List<Trigger> getTriggerByIdList(List triggerIds) {
        return triggerDao.getTriggerByIdList(triggerIds);
    }

    public List<Trigger> getTagTriggers(Tag tag) {
        return triggerDao.getTagTriggers(tag);
    }

    /**
     * 根据trigger中的字段查询trigger列表
     * @param trigger   trigger对象
     * @return  trigger列表
     */
    public List<Trigger> getTriggers(Trigger trigger) {
        return triggerDao.getTriggers(trigger);
    }

    /**
     * 获取trigger的filters
     * @param trigger   trigger对象
     * @return  filter列表
     */
    public List<VariableFilter> getTriggerFilters(Trigger trigger) {
        return triggerDao.getTriggerFilters(trigger);
    }

    /**
     * 获取trigger的连接对象
     * @param triggerId trigger的主键id
     * @return  连接对象列表
     */
    public List<TagTriggerLink> getLinkByTriggerId(Integer triggerId) {
        return triggerDao.getLinkByTriggerId(triggerId);
    }

    /**
     * 通过trigger主键删除trigger
     * @param triggerId     主键id
     * @return  影响行数
     */
    public int deleteByTriggerId(Integer triggerId) {
        return triggerDao.deleteByTriggerId(triggerId);
    }

    /**
     * 更新trigger
     * @param trigger  更新对象
     * @return      影响行数
     */
    public int updateTrigger(Trigger trigger) {
        return triggerDao.updateTrigger(trigger);
    }
}
