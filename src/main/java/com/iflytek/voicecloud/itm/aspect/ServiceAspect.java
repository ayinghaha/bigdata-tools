package com.iflytek.voicecloud.itm.aspect;

import com.iflytek.voicecloud.itm.common.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 根据 业务 进行切面操作
 */
@Aspect
@Component
public class ServiceAspect {

    /**
     * 切点设置为dao.analysis中的接口
     */
    @Pointcut("execution(* com.iflytek.voicecloud.itm.dao.analysis.*.*(..))")
    private void aspectJMethod(){}

    @Before("aspectJMethod()")
    public void doBefore(JoinPoint joinPoint){
        // 请求分析数据库时切换数据源为itmAnalysis
        String dbType = DataSourceContextHolder.getDbType();
        if (dbType == null || !dbType.equals("itmAnalysis")) {
            DataSourceContextHolder.setDbType("itmAnalysis");
        }
    }

    @After("aspectJMethod()")
    public void doAfter(JoinPoint joinPoint){
        // 离开 dao.analysis 后切换数据源为 itmConfig
        String dbType = DataSourceContextHolder.getDbType();
        if (dbType == null || !dbType.equals("itmConfig")) {
            DataSourceContextHolder.setDbType("itmConfig");
        }
    }

}
