package com.iflytek.voicecloud.analysis.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.voicecloud.analysis.utils.HttpTools;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.config.Group;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * 人群洞察接口
 */
@Controller
@RequestMapping("/statistic")
public class PopulationInsight {
    //对应接口 1.1

    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/taglist")
    public void populationGetTagList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数
        List<Group> groups = (List<Group>) request.getSession().getAttribute("groups");


        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数


        // 获取远程接口
        String result = HttpTools.getRemoteDataByGet("http://zeus.xfyun.cn/aac/taglist/");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

    //对应接口 2.1
    @RequestMapping("/preview")
    public void populationpreview(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        String query = request.getParameter("query");
        String target = request.getParameter("target");


        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("target", target);
        param.put("ver", "1.0");

        param.put("query", transAsJson(query));

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/insight", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }

    //对应接口 2.2.1
    @RequestMapping("/build")
    public void pupulationbuild(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数
        String query = request.getParameter("query");

        String tagname = request.getParameter("tagname");
        String userid = request.getParameter("userid");
        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("userid", userid);

        param.put("operation", "build");
        param.put("query", transAsJson(query));
        param.put("tagname", tagname);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/buildtag", param);
        Map<String, Object> map = JsonUtil.JsonToMap(result);
        Integer ret = (Integer) map.get("ret");
        if (ret.equals(0)) {

            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("token", token);
            param1.put("ver", "1.0");
            param1.put("operation", "insight");
            param1.put("tagid", Integer.parseInt((String) map.get("data")));
            param1.put("model", "0");

            // 获取远程接口


            HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/buildtag", param1);


        }
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }

    //  对应接口2.2.2

    @RequestMapping("/insight")
    public void populationinsight(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        //  JsonNode json = parseRequest(request);
        int tagid = Integer.parseInt(request.getParameter("tagid"));
        int model = Integer.parseInt(request.getParameter("model"));


        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "insight");
        param.put("tagid", tagid);
        param.put("model", model);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/buildtag", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }

    //对应接口 2.3.7
    @RequestMapping("/rebuild")
    public void populationrebuild(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        int tagid = Integer.parseInt(request.getParameter("tagid"));
        int model = Integer.parseInt(request.getParameter("model"));

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "rebuild");
        param.put("tagid", tagid);
        param.put("model", model);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/buildtag", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }

    //对应接口2.3.6
    @RequestMapping("/export")
    public void populationexport(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        int tagid = Integer.parseInt(request.getParameter("tagid"));

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "export");
        param.put("tagid", tagid);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/buildtag", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }


    @RequestMapping("/check")
    public void populationcheck(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        int tagid = Integer.parseInt(request.getParameter("tagid"));

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "check");
        param.put("tagid", tagid);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/buildtag", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }

    @RequestMapping("/delete")
    public void populationdelete(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int tagid = Integer.parseInt(request.getParameter("tagid"));

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("operation", "delete");
        param.put("tagid", tagid);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/buildtag", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }

    //对应接口5.3
    @RequestMapping("/improtid")
    public void populationGetImportId(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("task_type", "datalist");

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/import/import", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }


//对应接口2.2.3

    @RequestMapping("/result")
    public void populationgetresult(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        int tagid = Integer.parseInt(request.getParameter("tagid"));
        String idtype = request.getParameter("idtype");
        String target = request.getParameter("target");


        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("idtype", idtype);
        param.put("tagid", tagid);
        param.put("target", target);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/getresult", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }


    //对应接口2.3.9
    @RequestMapping("/usertaginfo")
    public void populationGetUserTagInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数

        String userid = request.getParameter("userid");

        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("userid", userid);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/gettaginfo", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }


    //对应接口2.3.8
    @RequestMapping(value = "/keyword")

    public void populationKeyWordExtend(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数


        String keyword = request.getParameter("keyword");
        Map map = request.getParameterMap();
        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("keyword", keyword);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/keywords", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }

    @RequestMapping(value = "/appname")

    public void populationAppnameExtend(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 接收参数

        //    JsonNode json = parseRequest(request);
        //  String keyword = json.get("keyword").asText();
        String appname = request.getParameter("appname");
        String token = "70F5E3AF1A6AD6B13375626DB5D0C123";
        if (token == null) {
            ResponseUtil.setResponseJson(response, new Message(-1, "用户未登录"));
            return;
        }

        // 构造参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        param.put("ver", "1.0");
        param.put("appname", appname);

        // 获取远程接口
        String result = HttpTools.getRemoteData("http://192.168.72.124:8080/aac/insight/appname", param);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(result);
    }


    public JsonNode transAsJson(String data) throws IOException {


        JsonNode json = mapper.readTree(data);
        return json;
    }

}
