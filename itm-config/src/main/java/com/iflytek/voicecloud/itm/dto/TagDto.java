package com.iflytek.voicecloud.itm.dto;

import com.iflytek.voicecloud.itm.entity.config.Tag;
import com.iflytek.voicecloud.itm.entity.config.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tag 数据返回格式
 */
public class TagDto {

    public static Map<String, Object> formatTagJson(Tag tag) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("id", tag.getId());
        resMap.put("name", tag.getName());
        resMap.put("type", tag.getType());
        resMap.put("action", tag.getAction());
        resMap.put("category", tag.getCategory());
        resMap.put("label", tag.getLabel());
        resMap.put("value", tag.getValue());
        resMap.put("isDebug", tag.getIsDebug());

        // 写入trigger元素
        List<Trigger> triggers = tag.getTriggers();
        List<Map<String, Object>> triggerMaps = new ArrayList<Map<String, Object>>();
        // 预防空指针异常
        if (triggers.get(0) != null) {
            for (Trigger trigger : triggers) {
                triggerMaps.add(TriggerDto.formatTriggerJson(trigger));
            }
        }
        resMap.put("triggers", triggerMaps);

        return resMap;
    }

}
