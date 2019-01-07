package com.xie.server.a;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("经过LoginInterceptor");
        HttpServletRequest  request2 = (HttpServletRequest) request;
        System.out.println("path:"+request2.getServletPath());
        Enumeration<String> headerNames = request2.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String s = headerNames.nextElement();
            String header = request2.getHeader(s);
            System.out.println("name:"+s+"["+header);
        }
//        if(true){
//            throw new RuntimeException("未登录");
//        }
        return true;
    }

}
