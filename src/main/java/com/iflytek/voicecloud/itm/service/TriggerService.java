package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.config.Tag;
import com.iflytek.voicecloud.itm.entity.config.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.config.Trigger;
import com.iflytek.voicecloud.itm.entity.config.VariableFilter;

import java.util.List;

/**
 * Created by jdshao on 2017/3/7
 */
public interface TriggerService {

    int addTrigger(Trigger trigger);

    Trigger getTriggerById(int id);

    List<Trigger> getTriggerByIdList(List triggerIds);

    List<Trigger> getTagTriggers(Tag tag);

    List<Trigger> getTriggers(Trigger trigger);

    List<VariableFilter> getTriggerFilters(Trigger trigger);

    List<TagTriggerLink> getLinkByTriggerId(Integer triggerId);

    int deleteByTriggerId(Integer triggerId);

    int updateTrigger(Trigger trigger);
}
