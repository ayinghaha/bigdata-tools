package com.iflytek.voicecloud.analysis.utils;

import com.iflytek.voicecloud.itm.utils.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 */
public class RpcApiUtil {

    private static ConnectionConfig connectionConfig = ConnectionConfig.custom().setBufferSize(4128).build();

    private static ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
        public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
            return 20 * 1000; // tomcat默认keepAliveTimeout为20s
        }
    };

    private static CloseableHttpClient httpClient = HttpClients.custom()
            .setKeepAliveStrategy(connectionKeepAliveStrategy)
            .setDefaultConnectionConfig(connectionConfig)
            .build();

    /**
     * 请求远程数据接口
     * @param url       请求地址
     * @param reqParam  请求参数
     * @return          返回数据
     */
    public static String getRemoteData(String url, Map<String, Object> reqParam) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Contetn-type", "application/json; charset=utf8");
        StringEntity entity = new StringEntity(JsonUtil.ObjectToJson(reqParam), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        return EntityUtils.toString(httpResponse.getEntity(), "utf-8");
    }

    /**
     * get方法请求
     * @param url   请求地址
     * @return      返回结果
     * @throws Exception   异常
     */
    public static String getRemoteDataByGet(String url ) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Contetn-type", "application/json; charset=utf8");

        HttpResponse httpResponse = httpClient.execute(httpGet);
        return EntityUtils.toString(httpResponse.getEntity());
    }

}
