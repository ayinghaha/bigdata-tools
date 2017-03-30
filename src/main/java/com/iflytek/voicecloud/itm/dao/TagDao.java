package com.iflytek.voicecloud.itm.dao;

import com.iflytek.voicecloud.itm.entity.Tag;
import com.iflytek.voicecloud.itm.entity.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.Trigger;

import java.util.List;

/**
 * Created by jdshao on 2017/3/8
 */
public interface TagDao {

    /**
     * 添加标签
     * @param tag Tag对象
     * @return 生成的主键id
     */
    int addTag(Tag tag);

    /**
     * 添加Tag和Trigger连接对象
     * @param tagTriggerLinks 连接对象列表
     * @return  插入结果
     */
    int addTagTriggerLink(List<TagTriggerLink> tagTriggerLinks);

    /**
     * 通过主键id查询Tag
     * @param id   主键id
     * @return  查询结果
     */
    Tag getTagById(int id);

    /**
     * 根据条件查询tag列表
     * @param tag  tag对象
     * @return  tag列表
     */
    List<Tag> getTagList(Tag tag);

    /**
     * 通过Tag对象查询绑定的Trigger
     * @param tag   Tag对象
     * @return  返回Trigger列表
     */
    List<Trigger> getTagTriggers(Tag tag);

    /**
     * 通过Tag主键删除Tag
     * @param tagId     tag主键
     * @return      影响行数
     */
    int deleteTagById(Integer tagId);

    /**
     * 更新Tag
     * @param tag   更新对象
     * @return      影响行数
     */
    int updateTag(Tag tag);

    /**
     * 删除Tag下的所有链接对象
     * @param tag   Tag对象
     * @return  影响行数
     */
    int deleteTagLink(Tag tag);

}
