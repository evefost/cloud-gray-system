package com.xie.server.a.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;


public class LoginInterceptor implements HandlerInterceptor {
    private  final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("经过LoginInterceptor path:{}",request.getServletPath());
//        if(true){
//            throw new RuntimeException("未登录");
//        }
        return true;
    }

}
