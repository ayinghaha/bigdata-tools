package com.iflytek.voicecloud.itm.service;

import com.iflytek.voicecloud.itm.entity.config.Tag;
import com.iflytek.voicecloud.itm.entity.config.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.config.Trigger;

import java.util.List;

/**
 * Created by jdshao on 2017/3/9
 */
public interface TagService {

    int addTag(Tag tag);

    int addTagTriggerLinks(List<TagTriggerLink> tagTriggerLinks);

    Tag getTagById(int id);

    List<Tag> getTagList(Tag tag);

    List<Trigger> getTagTriggers(Tag tag);

    int deleteTagById(Integer tagId);

    int updateTag(Tag tag);

    int deleteTagLink(Tag tag);

}
