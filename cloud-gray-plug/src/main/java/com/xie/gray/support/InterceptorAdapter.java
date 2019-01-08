package com.xie.gray.support;

import com.xie.gray.config.GrayProperties;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


/**
 * @author xieyang
 */
public  class InterceptorAdapter implements Callback {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final String FILTER_METHOD="doFilter";

    private final String INTERCEPTOR_METHOD="preHandle";

    PathMatcher pathMatcher = new AntPathMatcher();

    private Object target;

    private GrayProperties grayProperties;

    public InterceptorAdapter(Object target, GrayProperties grayProperties) {
        this.target = target;
        this.grayProperties = grayProperties;
    }



    /**
     * 如果是测度路径，跳过当前filter
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ServletException
     */
    protected Object doFilter(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, IOException, ServletException {
        //是否为doFilter 方法
        if (FILTER_METHOD.equals(method.getName())) {
            HttpServletRequest servletRequest = (HttpServletRequest) args[0];
            ServletResponse servletResponse = (ServletResponse) args[1];
            FilterChain filterChain = (FilterChain) args[2];
            String servletPath = servletRequest.getServletPath();
            //符合跳过规则，执行一个filter
            if (skipCurrentPath(servletPath)) {
                logger.debug("[{}] is gray test path ,skip filter of {}",servletPath,target.getClass().getSimpleName());
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                method.invoke(target, args);
            }
        } else {
            method.setAccessible(true);
            return method.invoke(target, args);
        }
        return null;
    }


    private boolean skipCurrentPath(String servletPath){

        Set<String> skipPaths = grayProperties.getSkipPaths();
        for(String pattern :skipPaths){
            if(pathMatcher.match(pattern,servletPath)){
                return true;
            }
        }
        return false;
    }

    /**
     * 如果是测试路径，跳过当前interceptor
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Exception
     */
    protected Object preHandle(Object proxy, Method method, Object[] args)
            throws Exception {
        //是否为preHandle 方法
        if (INTERCEPTOR_METHOD.equals(method.getName())) {
            HttpServletRequest servletRequest = (HttpServletRequest) args[0];
            String servletPath = servletRequest.getServletPath();
            //符合跳过规则，不执当前代理逻辑
            if (skipCurrentPath(servletPath)) {
                logger.debug("[{}] is gray test path ,skip interceptor of {}", servletPath, target.getClass().getSimpleName());
                return true;
            } else {
                return method.invoke(target, args);
            }
        } else {
            method.setAccessible(true);
            return method.invoke(target, args);
        }
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
