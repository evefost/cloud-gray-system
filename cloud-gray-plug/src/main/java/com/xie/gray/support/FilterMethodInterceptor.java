package com.xie.gray.support;


import com.xie.gray.config.GrayProperties;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * Created by xieyang on 18/7/14.
 */
public class FilterMethodInterceptor  extends InterceptorAdapter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public FilterMethodInterceptor(Object target, GrayProperties grayProperties) {
        super(target, grayProperties);
    }
    /**
     * 如果是测试链路，跳过该过滤器
     * @param proxy
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
      return doFilter(proxy,method,args);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return doFilter(proxy,method,args);
    }
}
