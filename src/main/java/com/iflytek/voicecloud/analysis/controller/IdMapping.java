package com.iflytek.voicecloud.analysis.controller;

/**
 * Created by liuying on 2017/6/22.
 */

import com.iflytek.voicecloud.analysis.utils.HttpTools;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.config.Group;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iflytek.voicecloud.analysis.utils.IdMappingConstant.*;
import static com.iflytek.voicecloud.analysis.utils.IdMappingConstant.SEPARATOR;
import static com.iflytek.voicecloud.analysis.utils.IdMappingConstant.ZERO;
import static com.iflytek.voicecloud.itm.utils.ResponseUtil.setResponseJson;

/**
 * ID贯通服务
 */
@Controller
@RequestMapping("/idmapping")
public class IdMapping {
    private static final Log LOG = LogFactory.getLog(IdMapping.class);

    @RequestMapping("/total_count")
    /**
     * 获取id数量查询接口
     */
    private void IdTotalCount(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> reqParam=createDefaultParams(request,response);
        // 获取远程接口
        String result = null;
        try {
            result = HttpTools.getRemoteData(ID_TOTAL_COUNT_URL, reqParam);
        } catch (Exception e) {
            LOG.error("获取数据失败！");
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            LOG.error("打印返回结果失败！");
            e.printStackTrace();
        }
        reqParam.clear();
    }
    @RequestMapping("/through_count")
    /**
     * 获取id打通数量查询接口
     */
    private void IdThroughCount(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> reqParam=createDefaultParams(request,response);
        String result = null;
        try {
            result = HttpTools.getRemoteData(ID_THROUGH_COUNT_URL, reqParam);
        } catch (Exception e) {
            LOG.error("获取数据失败！");
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            LOG.error("打印返回结果失败！");
            e.printStackTrace();
        };
        reqParam.clear();
    }

    /**
     *  创建基础请求参数map
     * @param request
     * @param response
     * @return
     */
    public  Map<String, Object> createDefaultParams(HttpServletRequest request,HttpServletResponse response){
        String token=getGroupToken(request,response);
        String version=setDefaultVal((String)request.getParameter(VERSION),VERSION_VAL);
        String[] time_range=getDateRange(TOTAL_MONTHS).split(SEPARATOR);
        String all=setDefaultVal((String)request.getParameter(ALL),ZERO);
        String cid=setDefaultVal((String)request.getParameter(CID),ZERO);
        String import_id=setDefaultVal((String)request.getParameter(IMPORT_ID),ZERO);

        Map<String, Object> params=new HashMap<String, Object>();
        params.put(TOKEN,token);
        params.put(VERSION,version);
        params.put(START_TIME,time_range[0]);
        params.put(END_TIME,time_range[1]);
        params.put(ALL,all);
        params.put(CID,cid);
        params.put(IMPORT_ID,import_id);
        return params;
    }

    /**
     * 根据groupId从客户列表中获取客户绑定的token
     * @param request       数据请求对象
     * @return Message  包含token字段
     */
    public String getGroupToken(HttpServletRequest request,HttpServletResponse response) {
        String groupId=(String)request.getParameter(GROUP_ID);
        String token=null;
        // 从session中保存的客户列表中获取对应的token
        List<Group> groups = (List<Group>) request.getSession().getAttribute(GROUPS);
        if(groups==null||groupId==null){
            try {
                setResponseJson(response,new Message(-1, "用户未登录！"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return token;
        }
        int id=Integer.valueOf(groupId);
        for (Group group : groups) {
            if (group.getId() == id) {
                token= group.getToken();
                break;
            }
        }
        if(token==null){
            try {
                setResponseJson(response, new Message(-2, "Group ID 未匹配到可用token!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    /**
     * 如果前台请求参数值为空，可以为其设置默认值
     * @param value
     * @param defaultVal
     * @return
     */
    public String setDefaultVal(String value,String defaultVal){
        if(value==null){
            LOG.warn("请求参数值为空，使用默认值替换: "+defaultVal+"!");
            value=defaultVal;
        }
        return value;
    }

    /**
     * 返回最近index月的起止日期
     * @param index
     * @return
     */
    public String getDateRange(int index){
        Calendar date=Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String end_time=df.format(date.getTime());
        date.add(Calendar.MONTH,0-index);
        String start_time=df.format(date.getTime());
        StringBuffer buf=new StringBuffer();
        buf.append(start_time).append(SEPARATOR).append(end_time);
        return buf.toString();
    }

}
