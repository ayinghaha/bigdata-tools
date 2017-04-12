package com.iflytek.voicecloud.itm.controller;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.dto.TagDto;
import com.iflytek.voicecloud.itm.dto.TriggerDto;
import com.iflytek.voicecloud.itm.entity.Tag;
import com.iflytek.voicecloud.itm.entity.TagTriggerLink;
import com.iflytek.voicecloud.itm.entity.Trigger;
import com.iflytek.voicecloud.itm.entity.VariableFilter;
import com.iflytek.voicecloud.itm.service.TagService;
import com.iflytek.voicecloud.itm.service.TriggerService;
import com.iflytek.voicecloud.itm.service.VariableFilterService;
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
    public void addTag(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String itmID = request.getParameter("itmID");
        String containerID = request.getParameter("containerID");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String action = request.getParameter("action");
        String category = request.getParameter("category");
        String label = request.getParameter("label");
        String value = request.getParameter("value");
        String isDebug = request.getParameter("isDebug");
        String triggerIdList = request.getParameter("triggerIdList");

        Message message = new Message();
        message.setState(-1);
        List<String> asList = Arrays.asList(tagTypeList);
        if (itmID == null || containerID == null || name == null || type == null || isDebug == null || triggerIdList == null || !asList.contains(type) ) {
            message.setData("参数不全或类型不正确");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (type.equals("Event") && (value == null || label == null || action == null || category == null)) {
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
                message.setState(-1);
                message.setData("绑定触发器不存在");
                ResponseUtil.setResponseJson(response, message);
                return ;
            }

            for (Trigger trigger : triggers) {
                // TODO 判断当前触发器是否属于当前用户
                if (!trigger.getItmID().equals("jdshao")) {
                    message.setState(-1);
                    message.setData("绑定触发器不属于当前用户");
                    ResponseUtil.setResponseJson(response, message);
                    return ;
                }
            }
        }

        // 添加Tag数据
        Tag tag = new Tag(itmID, containerID, name, type, action, category, label, value, isDebug, new Date(), new Date());
        int resKey = tagService.addTag(tag);
        if (resKey < 0) {
            message.setData("添加失败");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else {
            message.setState(1);
            message.setData("添加成功");
            // 生成连接对象
            List<TagTriggerLink> tagTriggerLinks = new ArrayList<TagTriggerLink>();
            for (Trigger trigger : triggers) {
                tagTriggerLinks.add(new TagTriggerLink(tag, trigger, new Date()));
            }
            // 保存Tag和Trigger连接对象
            int lastId = tagService.addTagTriggerLinks(tagTriggerLinks);
            if (lastId > 0) {
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

        Message message = new Message();
        message.setState(1);
        message.setData(resMap);
        ResponseUtil.setResponseJson(response, message);
    }

    @RequestMapping("/delete")
    public void deleteTag(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Message message = new Message();
        message.setState(-1);

        String id = request.getParameter("id");
        if (id == null) {
            message.setData("标签id为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        int tagId = Integer.parseInt(id);
        Tag tag = tagService.getTagById(tagId);
        if (tag == null) {
            message.setData("要删除的标签不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!tag.getItmID().equals("jdshao")) {
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

        Message message = new Message();
        message.setState(-1);

        if (tag.getId() == 0) {
            message.setData("标签的主键id为空");
            ResponseUtil.setResponseJson(response, message);
            return ;
        }

        Tag detectTag = tagService.getTagById(tag.getId());
        if (detectTag == null) {
            message.setData("要更新的标签不存在");
            ResponseUtil.setResponseJson(response, message);
            return ;
        } else if (!detectTag.getItmID().equals("jdshao")) {
            message.setData("当前用户没有权限操作");
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
        } else {
            message.setState(1);
            message.setData("更新成功");
        }

        // 更新tag和trigger对应关系
        String triggerIdList = request.getParameter("triggerIdList");
        if (triggerIdList != null) {
            List triggerIds = JsonUtil.JsonToStringList(triggerIdList);
            if (triggerIds.size() > 0) {
                // 查询要绑定的变量
                List<Trigger> triggers = triggerService.getTriggerByIdList(triggerIds);
                if (triggers.size() != triggerIds.size()) {
                    message.setState(-1);
                    message.setData("绑定触发器不存在或者重复绑定");
                    ResponseUtil.setResponseJson(response, message);
                    return ;
                }

                // 生成连接对象
                List<TagTriggerLink> tagTriggerLinks = new ArrayList<TagTriggerLink>();
                for (Trigger trigger : triggers) {
                    // TODO 判断当前触发器是否属于当前用户
                    if (!trigger.getItmID().equals("jdshao")) {
                        message.setState(-1);
                        message.setData("绑定触发器不属于当前用户");
                        ResponseUtil.setResponseJson(response, message);
                        return ;
                    }
                    tagTriggerLinks.add(new TagTriggerLink(tag, trigger, new Date()));
                }

                // 删除当前tag的所有连接对象
                tagService.deleteTagLink(tag);

                // 保存Tag和Trigger连接对象
                int lastId = tagService.addTagTriggerLinks(tagTriggerLinks);
                if (lastId > 0) {
                    message.setState(1);
                    message.setData("添加/更新成功");
                } else {
                    message.setData("添加/更新失败");
                }
            }
        }

        ResponseUtil.setResponseJson(response, message);
    }

}
