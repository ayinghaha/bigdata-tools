package com.iflytek.voicecloud.analysis.controller;

/**
 * Created by liuying on 2017/6/22
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.iflytek.voicecloud.analysis.utils.IdMappingConstant.TASK_TYPE;
import static com.iflytek.voicecloud.analysis.utils.IdMappingConstant.TOKEN;
import static com.iflytek.voicecloud.analysis.utils.IdMappingUtil.*;

/**
 * ID贯通服务
 */
@Controller
@RequestMapping("/idmapping")
public class IdMapping {
    @RequestMapping("/total_count")
    /**
     * 获取id数量接口
     */
    private void IdTotalCount(HttpServletRequest request, HttpServletResponse response){
        getRemoteDataByPost(ID_TOTAL_COUNT_URL,request,response);
    }
    @RequestMapping("/through_count")
    /**
     * 获取id打通数量接口
     */
    private void IdThroughCount(HttpServletRequest request, HttpServletResponse response){
        getRemoteDataByPost(ID_THROUGH_COUNT_URL,request,response);
    }
    @RequestMapping("/crowd_select")
    /**
     * 获取import_id列表(暂时未用到）
     */
    private void crowdSelect(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> reqParam=new HashMap<String,Object>();
        String token=getGroupToken(request,response);
        reqParam.put(TASK_TYPE,IMPORT_TASK_TYPE);
        reqParam.put(TOKEN,token);
        getRemoteDataByPost(IMPORT_QUERY_URL,reqParam,response);
    }
}
