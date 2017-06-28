package com.iflytek.voicecloud.analysis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.entity.config.Group;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.iflytek.voicecloud.analysis.utils.IdMappingConstant.*;

/**
 * Created by liuying on 2017/6/26
 */
public class IdMappingUtil {
    private static final Log LOG = LogFactory.getLog(IdMappingUtil.class);

    public static final String VERSION_VAL="1.0";
    public static final String ZERO="0";
    public static final int TOTAL_MONTHS=3;
    public static final String ID_TOTAL_COUNT_URL="http://zeus.xfyun.cn/id_analysis/total_count";
    public static final String ID_THROUGH_COUNT_URL="http://zeus.xfyun.cn/id_analysis/id_pair_through";
    public static final String IMPORT_QUERY_URL="http://192.168.72.18:62222/import/import";
    public static final String IMPORT_TASK_TYPE="datalist";
    public static final String SEPARATOR=",";

    private static CloseableHttpClient httpClient ;

    public static CloseableHttpClient getHttpClient(){
        if(httpClient==null){
            httpClient = HttpClients.custom()
                    .setDefaultConnectionConfig(ConnectionConfig.custom()
                            .setBufferSize(4128)
                            .build())
                    .build();

                ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
                    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                        return 20 * 1000; // tomcat默认keepAliveTimeout为20s
                    }
                };
                PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(20, TimeUnit.SECONDS);
                connManager.setMaxTotal(200);
                connManager.setDefaultMaxPerRoute(200);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(100 * 1000)
                        .setSocketTimeout(100 * 1000)
                        .setConnectionRequestTimeout(100 * 1000)
                        .build();
                HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
                httpClientBuilder.setConnectionManager(connManager);
                httpClientBuilder.setDefaultRequestConfig(requestConfig);
                httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
                httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy);
                httpClient = httpClientBuilder.build();
        }

        return httpClient;
    }

    /**
     * post方式请求远程数据接口
     * @param url       请求地址
     * @return          返回数据
     */
    public static void getRemoteDataByPost(String url,HttpServletRequest request, HttpServletResponse response ){
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Contetn-type", "application/json; charset=utf8");
        Map<String,Object> reqParam=createDefaultParams(request,response);
        if(reqParam.get(TOKEN)==null){
            return;
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(JsonUtil.ObjectToJson(reqParam), Charset.forName("UTF-8"));
        } catch (JsonProcessingException e) {
            LOG.error("process json string faild!");
            e.printStackTrace();
        }
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse httpResponse = null;
        String jsonRes=null;
        try {
            httpResponse = getHttpClient().execute(httpPost);
        } catch (IOException e) {
            LOG.error("execute post request faild!");
            e.printStackTrace();
        }
        try {
            jsonRes= EntityUtils.toString(httpResponse.getEntity(),"utf-8");
        } catch (IOException e) {
            LOG.error("get entity faild!");
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(jsonRes);
        } catch (IOException e) {
            LOG.error("write response faild!");
            e.printStackTrace();
        }
        reqParam.clear();
    }

    /**
     * post方式请求远程数据接口
     * @param url       请求地址
     * @return          返回数据
     */
    public static void getRemoteDataByPost(String url,Map<String,Object> reqParam, HttpServletResponse response )  {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Contetn-type", "application/json; charset=utf8");
        StringEntity entity = null;
        if(reqParam.get(TOKEN)==null){
            reqParam.clear();
            return;
        }
        try {
            entity = new StringEntity(JsonUtil.ObjectToJson(reqParam), Charset.forName("UTF-8"));
        } catch (JsonProcessingException e) {
            LOG.error("process json string faild!");
            e.printStackTrace();
        }
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse httpResponse = null;
        String jsonRes=null;
        try {
            httpResponse = getHttpClient().execute(httpPost);
        } catch (IOException e) {
            LOG.error("execute post request faild!");
            e.printStackTrace();
        }
        try {
            jsonRes= EntityUtils.toString(httpResponse.getEntity(),"utf-8");
        } catch (IOException e) {
            LOG.error("get entity faild!");
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(jsonRes);
        } catch (IOException e) {
            LOG.error("write response faild!");
            e.printStackTrace();
        }
        reqParam.clear();
    }
    //创建基础请求参数
    public static Map<String, Object> createDefaultParams(HttpServletRequest request,HttpServletResponse response){

        String token=getGroupToken(request,response);
        String version=setDefaultVal((String)request.getAttribute(VERSION),VERSION_VAL);
        String[] time_range=getDateRange(TOTAL_MONTHS).split(SEPARATOR);
        String all=setDefaultVal((String)request.getAttribute(ALL),ZERO);
        String cid=setDefaultVal((String)request.getAttribute(CID),ZERO);
        String import_id=setDefaultVal((String)request.getAttribute(IMPORT_ID),ZERO);

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

    //返回最近index月的起止日期
    public static String getDateRange(int index){
        Calendar date=Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String end_time=df.format(date.getTime());
        date.add(Calendar.MONTH,0-index);
        String start_time=df.format(date.getTime());
        StringBuffer buf=new StringBuffer();
        buf.append(start_time).append(SEPARATOR).append(end_time);
        return buf.toString();
    }

    public static String setDefaultVal(String value,String defaultVal){
        if(value==null){
            LOG.warn("param value is null，use default value: "+defaultVal+"!");
            value=defaultVal;
        }
        return value;
    }

    /**
     * 根据groupId从客户列表中获取客户绑定的token
     * @param request       数据请求对象
     * @return Message  包含token字段
     */
    public static String getGroupToken(HttpServletRequest request,HttpServletResponse response) {
        String groupId=(String)request.getSession().getAttribute(GROUP_ID);
        String token=null;
        // 从session中保存的客户列表中获取对应的token
        List<Group> groups = (List<Group>) request.getSession().getAttribute(GROUPS);
        if(groups==null||groupId==null){
            LOG.error("groups :"+groups+";groupID :"+groupId+"!");
            setResponseJson(response,new Message(-1, "User is not logged in!"));
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
            setResponseJson(response, new Message(-1, "Group ID is not available!"));
        }
        return token;
    }

    public static void setResponseJson(HttpServletResponse response, Message message)  {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            LOG.error("get response message faild!");
            e.printStackTrace();
        }
        String resJson = null;
        try {
            resJson = JsonUtil.ObjectToJson(message);
        } catch (JsonProcessingException e) {
            LOG.error("process json string faild!");
            e.printStackTrace();
        }
        out.write(resJson);
    }
    
}
