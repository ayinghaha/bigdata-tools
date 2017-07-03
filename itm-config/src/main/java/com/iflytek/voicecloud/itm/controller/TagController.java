package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.dto.TagDto;
import com.iflytek.voicecloud.itm.entity.config.Group;
import com.iflytek.voicecloud.itm.entity.config.Tag;
import com.iflytek.voicecloud.itm.entity.config.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.config.Trigger;
import com.iflytek.voicecloud.itm.service.TagService;
import com.iflytek.voicecloud.itm.service.TriggerService;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Tag Controller
 */
@Controller
@RequestMapping("/tag")
public class TagController {

    /**
     * 绑定的TagService对象
     */
    @Autowired
    TagService tagService;

    /**
     * 绑定的TriggerService对象
     */
    @Autowired
    TriggerService triggerService;

    private static final String[] tagTypeList = {"Event", "HTML", "PageView"};


    @RequestMapping("/add")
    public void addTag(HttpServletRequest request, HttpServletResponse response, Tag tag) throws Exception {

        String triggerIdList = request.getParameter("triggerIdList");

        Message message = new Message(-1, "");
        List<String> asList = Arrays.asList(tagTypeList);
        if (tag.getItmID() == null || tag.getContainerID() == null || tag.getName() == null || tag.getType() == null || tag.getIsDebug() == null || triggerIdList == null || !asList.contains(tag.getType()) ) {
            message.setData("参数不全或类型不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (tag.getType().equals("Event") && (tag.getValue() == null || tag.getLabel() == null || tag.getAction() == null || tag.getCategory() == null)) {
            message.setData("类型为Event时，value、lable、action、category不能为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 当存在trigger时建立Tag和trigger联系
        // 新建trigger和filter关系
        List triggerIds = JsonUtil.JsonToStringList(triggerIdList);
        List<Trigger> triggers = triggerService.getTriggerByIdList(triggerIds);
        if (triggerIds.size() > 0) {
            // 查询要绑定的变量
            if (triggers.size() != triggerIds.size()) {
                message.setData("绑定触发器不存在");
                ResponseUtil.setResponseJson(response, message);
                return ;
            }

            for (Trigger trigger : triggers) {
                // TODO 判断当前触发器是否属于当前用户
                if (!trigger.getItmID().equals(tag.getItmID())) {
                    message.setData("绑定触发器不属于当前用户");
                    ResponseUtil.setResponseJson(response, message);
                    return ;
                }
            }
        }

        // 检测tag名称是否重复
        if (detectTagName(tag)) {
            message.setData("当前Container账号下存在同名Tag");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 添加Tag数据
        tag.setRegistTime(new Date());
        tag.setUpdateTime(new Date());
        if (tagService.addTag(tag) < 0) {
            message.setData("添加失败");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else {
            // 生成连接对象
            List<TagTriggerLink> tagTriggerLinks = new ArrayList<TagTriggerLink>();
            for (Trigger trigger : triggers) {
                tagTriggerLinks.add(new TagTriggerLink(tag, trigger, new Date()));
            }
            // 保存Tag和Trigger连接对象
            if (tagService.addTagTriggerLinks(tagTriggerLinks) > 0) {
                message.setState(1);
                message.setData("添加成功");
            } else {
                message.setData("添加失败");
            }
        }

        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/get")
    public void getTag(HttpServletRequest request, HttpServletResponse response, Tag tag) throws  Exception {

        List<Tag> tags = tagService.getTagList(tag);
        // 将trigger赋值到tag中
        for (int i = 0; i < tags.size(); i ++) {
            List<Trigger> triggers = triggerService.getTagTriggers(tags.get(i));
            tags.get(i).setTriggers(triggers);
        }

        // 格式化返回结构
        List<Map<String, Object>> resMap = new ArrayList<Map<String, Object>>();
        for (Tag tagItem : tags) {
            resMap.add(TagDto.formatTagJson(tagItem));
        }

        Message message = new Message(1, resMap);
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/delete")
    public void deleteTag(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message(-1, "");
        String id = request.getParameter("id");
        if (id == null) {
            message.setData("标签id为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        int tagId = Integer.parseInt(id);
        Tag tag = tagService.getTagById(tagId);

        // 获取用户所有的itmID
        List<Group> groupList = (List<Group>) request.getSession().getAttribute("groups");
        List<String> itmIdList = new ArrayList<String>();
        for (Group group : groupList) {
            itmIdList.add(group.getItmID());
        }

        if (tag == null) {
            message.setData("要删除的标签不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!itmIdList.contains(tag.getItmID())) {
            message.setData("当前登录用户没有删除当前标签权限");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // Tag 可以直接删除 TagTriggerLink中外键为 on casecade 会一并删除
        int delRes = tagService.deleteTagById(tagId);
        if (delRes > 0) {
            message.setState(1);
            message.setData("删除成功");
        } else {
            message.setData("删除失败");
        }

        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/update")
    public void updateTag(HttpServletRequest request, HttpServletResponse response, Tag tag) throws Exception {

        Message message = new Message(-1, "");

        if (tag.getId() == 0) {
            message.setData("标签的主键id为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        Tag detectTag = tagService.getTagById(tag.getId());

        // 获取用户所有的itmID
        List<Group> groupList = (List<Group>) request.getSession().getAttribute("groups");
        List<String> itmIdList = new ArrayList<String>();
        for (Group group : groupList) {
            itmIdList.add(group.getItmID());
        }

        if (detectTag == null) {
            message.setData("要更新的标签不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!itmIdList.contains(detectTag.getItmID())) {
            message.setData("当前用户没有权限操作");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测tag名称是否重复
        if (detectTagName(tag)) {
            message.setData("当前Container账号下存在同名Tag");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 检测修改类型和event类型下的字段是否为空
        List<String> asList = Arrays.asList(tagTypeList);
        if (tag.getType() == null || !asList.contains(tag.getType())) {
            message.setData("类型为空或类型不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (tag.getType().equals("Event") && (tag.getValue() == null || tag.getLabel() == null || tag.getAction() == null || tag.getCategory() == null)) {
            message.setData("类型为Event时，value、lable、action、category不能为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 直接更新Tag对象
        int updateRes = tagService.updateTag(tag);
        if (updateRes == 0) {
            message.setData("更新失败");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        // 更新tag和trigger对应关系
        String triggerIdList = request.getParameter("triggerIdList");
        if (triggerIdList != null) {
            List triggerIds = JsonUtil.JsonToStringList(triggerIdList);
            if (triggerIds.size() > 0) {
                // 查询要绑定的变量
                List<Trigger> triggers = triggerService.getTriggerByIdList(triggerIds);
                if (triggers.size() != triggerIds.size()) {
                    message.setData("绑定触发器不存在或者重复绑定");
                    ResponseUtil.setResponseJson(response, message);
                    return ;
                }

                // 生成连接对象
                List<TagTriggerLink> tagTriggerLinks = new ArrayList<TagTriggerLink>();
                for (Trigger trigger : triggers) {
                    // TODO 判断当前触发器是否属于当前用户
                    if (!trigger.getItmID().equals(tag.getItmID())) {
                        message.setData("绑定触发器不属于当前用户");
                        ResponseUtil.setResponseJson(response, message);
                        return ;
                    }
                    tagTriggerLinks.add(new TagTriggerLink(tag, trigger, new Date()));
                }

                // 删除当前tag的所有连接对象
                tagService.deleteTagLink(tag);

                // 保存Tag和Trigger连接对象
                if (tagService.addTagTriggerLinks(tagTriggerLinks) > 0) {
                    message.setState(1);
                    message.setData("添加/更新成功");
                } else {
                    message.setData("添加/更新失败");
                }
            }
        }
        ResponseUtil.setResponseJson(response, message);
    }

    /**
     * 检测 Tag 名称是否重复
     * @param tag   Tag对象
     * @return  true 重复 false 不重复
     */
    private boolean detectTagName(Tag tag) {
        Tag detectTag = new Tag(tag.getItmID(), tag.getContainerID(), tag.getName(), null, null, null, null, null, null, null, null);
        List<Tag> detectTags = tagService.getTagList(detectTag);
        if (detectTags.size() > 0) {
            int detectId = detectTags.get(0).getId();
            if (detectId != tag.getId()) {
                return true;
            }
        }
        return false;
    }

}
