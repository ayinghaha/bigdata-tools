package com.iflytek.voicecloud.itm.filter;

import com.iflytek.voicecloud.itm.dto.Message;
import com.iflytek.voicecloud.itm.utils.ResponseUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户操作登录检测过滤器
 */
public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 用户要过滤的URI
        String[] filterURI = new String[] { "variable", "filter", "tag", "trigger", "container", "user", "group"};
        // 请求的URI
        String URI = request.getRequestURI();
        // 是否过滤
        boolean doFilter = false;
        for (String uri : filterURI) {
            if (URI.contains(uri)) {
                doFilter = true;
                break;
            }
        }

        if (doFilter) {
            // 执行过滤
            String user = (String) request.getSession().getAttribute("userName");
            Message message = new Message(-2, "");
            if (user == null) {
                message.setData("用户未登录");
            } else if(!user.equals("admin") && (URI.contains("user") || URI.contains("group") || URI.contains("setUserPrivileges"))) {
                // 非admin用户不能访问 user 和 group 模块
                message.setData("普通用户禁止访问次模块");
            } else {
                // 条件满足放行
                filterChain.doFilter(request, response);
                return ;
            }
            ResponseUtil.setResponseJson(response, message);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
