package com.iflytek.voicecloud.itm.service.impl;

import com.iflytek.voicecloud.itm.dao.config.TagDao;
import com.iflytek.voicecloud.itm.entity.config.Tag;
import com.iflytek.voicecloud.itm.entity.config.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.config.Trigger;
import com.iflytek.voicecloud.itm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tag 模块数据访问层
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagDao tagDao;

    /**
     * 添加Tag
     * @param tag tag对象
     * @return  生成主键id
     */
    public int addTag(Tag tag) {
        return tagDao.addTag(tag);
    }

    /**
     * 添加Tag和Trigger连接数据
     * @param tagTriggerLinks  Tag和Trigger连接对象
     * @return  插入结果
     */
    public int addTagTriggerLinks(List<TagTriggerLink> tagTriggerLinks) {
        return tagDao.addTagTriggerLink(tagTriggerLinks);
    }

    /**
     * 通过主键id查询Tag
     * @param id   主键id
     * @return  查询结果
     */
    public Tag getTagById(int id) {
        return tagDao.getTagById(id);
    }

    /**
     * 根据条件查询tag列表
     * @param tag  tag对象
     * @return  tag列表
     */
    public List<Tag> getTagList(Tag tag) {
        return tagDao.getTagList(tag);
    }

    /**
     * 通过Tag对象查询绑定的Trigger
     * @param tag   Tag对象
     * @return  返回Trigger列表
     */
    public List<Trigger> getTagTriggers(Tag tag) {
        return tagDao.getTagTriggers(tag);
    }

    /**
     * 通过Tag主键删除Tag
     * @param tagId     tag主键
     * @return      影响行数
     */
    public int deleteTagById(Integer tagId) {
        return tagDao.deleteTagById(tagId);
    }

    /**
     * 更新Tag
     * @param tag   更新对象
     * @return      影响行数
     */
    public int updateTag(Tag tag) {
        return tagDao.updateTag(tag);
    }

    /**
     * 删除Tag下的所有链接对象
     * @param tag   Tag对象
     * @return  影响行数
     */
    public int deleteTagLink(Tag tag) {
        return tagDao.deleteTagLink(tag);
    }
}
