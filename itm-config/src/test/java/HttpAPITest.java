import com.iflytek.voicecloud.itm.utils.JsonUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jdshao on 2017/4/20
 */
public class HttpAPITest {

    @Test
    public void testAPIRegist() throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://zeus.xfyun.cn/insight/acl");
        httpPost.setHeader("Contetn-type", "application/json; charset=utf8");

        Map<String, String> param = new HashMap<String, String>();
        param.put("user", " 55555");
        param.put("passwd", "iflytek");
        param.put("operation", "regist");
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
        Map<String, Object> resObj = JsonUtil.JsonToMap(result);
        System.out.println(resObj);
    }

}
