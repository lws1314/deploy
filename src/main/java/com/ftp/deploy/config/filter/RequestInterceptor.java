package com.ftp.deploy.config.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求操作监听
 *
 * @author ZERO
 */
@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenStr = request.getHeader("token");
        String userAgent = request.getHeader("user-agent");
        log.info("-----------------------------------用户请求参数--------------------------------------------\n"+
                " requestPath: " + request.getRequestURL()
                + " parameter: " + JSON.toJSONString(request.getParameterMap())
                + " userAgent: " + userAgent
        );
        return true;
    }


}
