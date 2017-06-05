package com.iflytek.voicecloud.analysis.controller;

import com.iflytek.voicecloud.analysis.utils.FTPUtil;
import com.iflytek.voicecloud.analysis.utils.RpcApiUtil;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.Group;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import com.iflytek.voicecloud.itm.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据导入
 */
@Controller
@RequestMapping("/import")
public class DataImport {

    /**
     * 服务器文件上传地址
     */
    private static String uploadPath = "/var/www/itm/upload/";
//    private static String uploadPath = "C:/Users/jdshao/Desktop/ITM/upload";

    /**
     * 远程数据接口
     */
    private static String remoteURL = "http://192.168.72.18:62222/import/import";
//    private static String remoteURL = "http://zeus.xfyun.cn/import/import";

    @RequestMapping("/request")
    public void importRequest(HttpServletRequest request, HttpServletResponse response, @RequestParam("importFile") CommonsMultipartFile uploadFile) throws Exception {

        String taskType = request.getParameter("taskType");
        String dataComment = request.getParameter("dataComment");
        String importDataType = request.getParameter("importDataType");
        String dataName = request.getParameter("dataName");
        String groupId = request.getParameter("groupId");
        if ( StringUtil.isStringNull(taskType) ||
             StringUtil.isStringNull(dataComment) ||
             StringUtil.isStringNull(importDataType) ||
             StringUtil.isStringNull(dataName) ||
             StringUtil.isStringNull(groupId)) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        }

        // 获取token
        int gId = Integer.parseInt(groupId);
        Message innerMessage = getGroupToken(request, gId);
        if (innerMessage.getState() != 1) {
            ResponseUtil.setResponseJson(response, innerMessage);
            return ;
        }
        String token = (String) innerMessage.getData();

        // 接收上传文件并存储至指定位置
        String targetPath = uploadPath + uploadFile.getOriginalFilename();
        File targetFile = new File(targetPath);
        uploadFile.transferTo(targetFile);

        // 根据时间戳设置文件名
        Date current = new Date();
        String targetFileName = String.valueOf(Long.toHexString(current.getTime()/1000)) + "_" + uploadFile.getOriginalFilename();

        // 上传至FTP服务器
        Message uploadMessage = FTPUtil.uploadFileToFTPServer(targetFile, targetFileName);
        if (uploadMessage.getState() != 1) {
            ResponseUtil.setResponseJson(response, uploadMessage);
            return ;
        }

        // 拼装参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("task_type", taskType);
        paramMap.put("token", token);
        paramMap.put("data_name", dataName);
        paramMap.put("data_comment", dataComment);
        paramMap.put("import_data_type", importDataType);
        paramMap.put("import_ftp_path", "ftp://36.7.172.10/data/aac_upload/" + targetFileName);
        // 请求远程接口
        try {
            String requestRes = RpcApiUtil.getRemoteData(remoteURL, paramMap);
            Map<String, Object> resultMap = JsonUtil.JsonToMap(requestRes);
            ResponseUtil.setResponseJson(response, new Message(1, resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.setResponseJson(response, new Message(-1, "请求远程接口失败"));
        }
    }

    @RequestMapping("/combineInterface")
    public void combineInterface(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String taskType = request.getParameter("taskType");
        String importId = request.getParameter("importId");
        String groupId = request.getParameter("groupId");
        if ( StringUtil.isStringNull(taskType) ||
             StringUtil.isStringNull(importId) ||
             StringUtil.isStringNull(groupId)) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        }

        // 获取token
        int gId = Integer.parseInt(groupId);
        Message innerMessage = getGroupToken(request, gId);
        if (innerMessage.getState() != 1) {
            ResponseUtil.setResponseJson(response, innerMessage);
            return ;
        }
        String token = (String) innerMessage.getData();

        // 拼装参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("task_type", taskType);
        paramMap.put("token", token);
        paramMap.put("import_id", importId);

        // 请求远程接口
        try {
            String requestRes = RpcApiUtil.getRemoteData(remoteURL, paramMap);
            Map<String, Object> resultMap = JsonUtil.JsonToMap(requestRes);
            ResponseUtil.setResponseJson(response, new Message(1, resultMap));
        } catch (Exception e) {
            ResponseUtil.setResponseJson(response, new Message(-1, "请求远程接口失败"));
        }
    }

    @RequestMapping("/dataquery")
    public void importAllDataQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String taskType = request.getParameter("taskType");
        String groupId = request.getParameter("groupId");
        if ( StringUtil.isStringNull(taskType) ||
                StringUtil.isStringNull(groupId)) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        }

        // 获取token
        int gId = Integer.parseInt(groupId);
        Message innerMessage = getGroupToken(request, gId);
        if (innerMessage.getState() != 1) {
            ResponseUtil.setResponseJson(response, innerMessage);
            return ;
        }
        String token = (String) innerMessage.getData();

        // 拼装参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("task_type", taskType);
        paramMap.put("token", token);

        // 请求远程接口
        try {
            String requestRes = RpcApiUtil.getRemoteData(remoteURL, paramMap);
            Map<String, Object> resultMap = JsonUtil.JsonToMap(requestRes);
            ResponseUtil.setResponseJson(response, new Message(1, resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.setResponseJson(response, new Message(-1, "请求远程接口失败"));
        }
    }

    @RequestMapping("/export")
    public void exportRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String taskType = request.getParameter("taskType");
        String exportId = request.getParameter("exportId");
        String groupId = request.getParameter("groupId");
        String exportDataType = request.getParameter("exportDataType");
        if ( StringUtil.isStringNull(taskType) ||
                StringUtil.isStringNull(exportId) ||
                StringUtil.isStringNull(exportDataType) ||
                StringUtil.isStringNull(groupId)) {
            ResponseUtil.setResponseJson(response, new Message(-1, "参数不全"));
            return ;
        }

        // 获取token
        int gId = Integer.parseInt(groupId);
        Message innerMessage = getGroupToken(request, gId);
        if (innerMessage.getState() != 1) {
            ResponseUtil.setResponseJson(response, innerMessage);
            return ;
        }
        String token = (String) innerMessage.getData();

        // 拼装参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("task_type", taskType);
        paramMap.put("token", token);
        paramMap.put("export_id", exportId);
        paramMap.put("export_data_type", exportDataType);
        paramMap.put("export_ftp_path", "ftp://36.7.172.10/data/down/myimei.txt");

        // 请求远程接口
        try {
            String requestRes = RpcApiUtil.getRemoteData(remoteURL, paramMap);
            Map<String, Object> resultMap = JsonUtil.JsonToMap(requestRes);
            ResponseUtil.setResponseJson(response, new Message(1, resultMap));
        } catch (Exception e) {
            ResponseUtil.setResponseJson(response, new Message(-1, "请求远程接口失败"));
        }
    }

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
            return new Message(-2, "用户未登录");
        }

        Message message = new Message(-1, "客户id不存在");
        for (Group group : groups) {
            if (group.getId() == groupId) {
                if (group.getToken() == null || group.getToken().equals("")) {
                    message.setData("客户token为空");
                } else {
                    message.setState(1);
                    message.setData(group.getToken());
                }
                break;
            }
        }
        return message;
    }
}
