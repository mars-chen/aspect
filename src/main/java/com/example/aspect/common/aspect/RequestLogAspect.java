package com.example.aspect.common.aspect;

import com.alibaba.fastjson.JSON;
import com.example.aspect.common.annotation.RequestLog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class RequestLogAspect {

    private Object result;

    private Long beginTime;


    @Before(value = "@annotation(r)")
    public void doBefore(JoinPoint joinPoint,RequestLog r){
        //log请求
        this.beginTime = System.currentTimeMillis();
        if (r.request()){
            logRequest(joinPoint);
        }
    }

    @Around("@annotation(com.example.aspect.common.annotation.RequestLog)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //执行程序
        result = joinPoint.proceed();
        return result;
    }

    @After(value = "@annotation(r)")
    public void doAfter(RequestLog r) {
        //log响应
        Map logMap = new HashMap();
        if (r.response()){
            logMap.put("response", result);
        }
        logMap.put("consume", System.currentTimeMillis() - beginTime);
        log.info("aspect:response:{}", JSON.toJSONString(logMap));
    }

    private void logRequest(JoinPoint joinPoint) {
        LogEntity logEntity = new LogEntity();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logEntity.setRequestUrl(request.getRequestURL().toString());
        logEntity.setRequestMethod(request.getMethod());
        logEntity.setRemoteAddress(request.getRemoteAddr());
        if (RequestMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            Enumeration<String> enumeration = request.getParameterNames();
            Map<String, String> paramMap = new HashMap<>();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                paramMap.put(key, request.getParameter(key));
            }
            logEntity.setParameter(paramMap);
        } else {
            logEntity.setRequestBody(joinPoint.getArgs());
        }
        log.info("aspect:request:{}", JSON.toJSONString(logEntity));
    }
    @Data
    static class LogEntity {

        private String remoteAddress;

        private String requestUrl;

        private String requestMethod;

        private Map<String, String> parameter;

        private Map<String, String> headers;

        private Object[] requestBody;
    }

}