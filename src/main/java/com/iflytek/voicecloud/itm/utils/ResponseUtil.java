package com.iflytek.voicecloud.itm.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.iflytek.voicecloud.itm.dto.Message;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jdshao on 2017/3/7
 */
public class ResponseUtil {

    public static void setResponseJson(HttpServletResponse response, Message message) throws IOException, JsonParseException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        String resJson = JsonUtil.ObjectToJson(message);
        out.write(resJson);
    }

}
