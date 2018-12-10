package com.xie.gray.support;


import com.xie.gray.config.GrayProperties;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by xieyang on 18/7/14.
 */
public class HandlerInterceptorMethodInterceptor extends InterceptorAdapter {


    public HandlerInterceptorMethodInterceptor(Object target, GrayProperties grayProperties) {
        super(target, grayProperties);
    }

    /**
     * 如果是测试链路，跳过该拦截器
     *
     * @param proxy
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return preHandle(proxy, method, args);
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return preHandle(proxy, method, args);
    }
}
