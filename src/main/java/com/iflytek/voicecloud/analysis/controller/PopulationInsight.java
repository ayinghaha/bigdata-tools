package com.iflytek.voicecloud.analysis.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.voicecloud.analysis.utils.RpcApiUtil;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 人群洞察接口
 */
@Controller
@RequestMapping("/statistic")
public class PopulationInsight {

    /**
     * 根据groupId从客户列表中获取客户绑定的token
     * @param request       数据请求对象
     * @param groupId       查询客户id
     * @return Message  包含token字段
     */
    private Message getGroupToken(HttpServletRequest request, int groupId) {

        // 从session中保存的客户列表中获取对应的token
        List<Group> groups = (List<Group>) request.getSession().getAttribute("groups");
        if (groups == null) {
            return new Message(-1, "用户未登录");
        }

        Message message = new Message(-1, "客户id不存在");
        for (Group group : groups) {
            if (group.getId() == groupId) {
                message.setState(1);
                message.setData(group.getToken());
                break;
            }
        }
        return message;
    }

    public static JsonNode parseRequest(HttpServletRequest request) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(),"utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JsonNode json = mapper.readTree(sb.toString());
        return  json;
    }

    @RequestMapping("/taglist")
    public void populationGetTagList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数
        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 获取远程接口
        String result = RpcApiUtil.getRemoteDataByGet("http://zeus.xfyun.cn/aac/taglist/");

        response.getWriter().write(result);
    }

    //对应接口 2.1
    @RequestMapping("/preview")
    public void populationpreview(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


//        String query = request.getParameter("query");
//        String target = request.getParameter("target");

        JsonNode json =  parseRequest(request);
        JsonNode queryNode =   json.get("query");
        String target = json.get("target").asText();

//        List<String[]> queryList = JsonUtil.JsonToStringArrayList(query);
//        if (query == null || target == null) {
//            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
//            return ;
//        }
        // session中保存的token
        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("target", target);
        param.put("ver", "1.0");

        param.put("query", queryNode);

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/insight", param);

        response.getWriter().write(result);
    }

    //对应接口 2.2.1
    @RequestMapping("/build")
    public void pupulationbuild(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        JsonNode json =  parseRequest(request);
        JsonNode queryNode =   json.get("query");
        String tagname = json.get("tagname").asText();


        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "build");
        param.put("query", queryNode);
        param.put("tagname", tagname);

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/buildtag", param);

        response.getWriter().write(result);
    }
    //对应接口2.2.2
    @RequestMapping("/insight")
    public void populationinsight(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        JsonNode json =  parseRequest(request);
        int tagid = json.get("tagid").asInt();
        int model =json.get("model").asInt();

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "insight");
        param.put("tagid", tagid);
        param.put("model", model);

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/buildtag", param);

        response.getWriter().write(result);
    }

    //对应接口 2.3.7
    @RequestMapping("/rebuild")
    public void populationrebuild(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数



        JsonNode json =  parseRequest(request);
        int tagid = json.get("tagid").asInt();
        int model =json.get("model").asInt();

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "rebuild");
        param.put("tagid", tagid);
        param.put("model", model);

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/buildtag", param);

        response.getWriter().write(result);
    }
    //对应接口2.3.6
    @RequestMapping("/export")
    public void populationexport(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数




        JsonNode json =  parseRequest(request);
        int tagid = json.get("tagid").asInt();

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "export");
        param.put("tagid", tagid);

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/buildtag", param);

        response.getWriter().write(result);
    }
    //对应接口5.3
    @RequestMapping("/improtid")
    public void populationGetImportId(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数





        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("task_type", "datalist");

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/import/import", param);

        response.getWriter().write(result);
    }


//对应接口2.2.3

    @RequestMapping("/result")
    public void populationgetresult(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数



        JsonNode json =  parseRequest(request);
        int tagid = json.get("tagid").asInt();
        String idtype =json.get("idtype").asText();
        String target =json.get("target").asText();

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("idtype", idtype);
        param.put("tagid", tagid);
        param.put("target", target);

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/getresult", param);

        response.getWriter().write(result);
    }



    //对应接口2.3.9
    @RequestMapping("/usertaginfo")
    public void populationGetUserTagInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数



        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/gettaginfo", param);

        response.getWriter().write(result);
    }

    //对应接口2.3.8
    @RequestMapping("/keyword")
    public void populationKeyWordExtend(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数

        JsonNode json =  parseRequest(request);
        String keyword =json.get("keyword").toString();

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return ;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("keyword",keyword);

        // 获取远程接口
        String result = RpcApiUtil.getRemoteData("http://zeus.xfyun.cn/aac/insight/keywords", param);

        response.getWriter().write(result);
    }



}
