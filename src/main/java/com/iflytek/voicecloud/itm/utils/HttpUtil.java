package com.iflytek.voicecloud.itm.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/21
 */
public class HttpUtil {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * 获取远程接口返回
     * @param url       请求地址
     * @param param     参数Map
     * @return      返回结果
     */
    public static String getRPCResponse(String url, Map<String, String> param) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Contetn-type", "application/json; charset=utf8");
        // 构建消息实体
        StringEntity entity = new StringEntity(JsonUtil.ObjectToJson(param), Charset.forName("UTF-8"));
        // 发送Json格式的数据请求
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        // 发送请求
        httpPost.setEntity(entity);
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            return "请求失败";
        }
    }

}
