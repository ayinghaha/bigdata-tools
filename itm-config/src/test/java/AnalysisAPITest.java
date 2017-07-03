import com.iflytek.voicecloud.analysis.utils.RpcApiUtil;
import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import com.iflytek.voicecloud.itm.utils.StringUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jdshao on 2017/5/16
 */
public class AnalysisAPITest {

    @Test
    public void testInsightAPI() throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://zeus.xfyun.cn/insight/insight");
        httpPost.setHeader("Contetn-type", "application/json; charset=utf8");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "DC1EAB78C56B7015E768046B981B362D");
        param.put("ver", "1.0");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        ArrayList<String[]> strList = new ArrayList<String[]>();
        strList.add(new String[]{"5062"});
        queryMap.put("dmptag", strList);
        param.put("query", queryMap);

        // 构建消息实体
        StringEntity entity = new StringEntity(JsonUtil.ObjectToJson(param), Charset.forName("UTF-8"));
        System.out.println(JsonUtil.ObjectToJson(param));
        entity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        Header[] headers = httpResponse.getAllHeaders();
        for (Header header : headers) {
            System.out.println(header.getName() + ":" + header.getValue());
        }
        String result = EntityUtils.toString(httpResponse.getEntity());
    }

    @Test
    public void testDataList() throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("task_type", "datalist");
        param.put("token", "9a25f8ec5743d18cc9d59ce793ca8d76");

        HttpPost httpPost = new HttpPost("http://zeus.xfyun.cn/import/import");
        httpPost.setHeader("Contetn-type", "application/json; charset=utf8");
        StringEntity entity = new StringEntity(JsonUtil.ObjectToJson(param), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String result =  EntityUtils.toString(httpResponse.getEntity(), "utf-8");

        System.out.println(result);
    }

    @Test
    public void testString() throws Exception {
        String newPassword = "jdshaoniubi123";
        System.out.println(newPassword.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$"));
    }

}
