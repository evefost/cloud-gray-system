package com.xie.gray.support;

import com.xie.gray.config.GrayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.Filter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理链测试接口跳过所有过滤器和拦截器
 *
 * @author xie yang
 * @date 2018/10/29-15:59
 */
public class InterceptorBeanProcessor implements BeanPostProcessor {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Map<String, Object> baseTypes = new HashMap<>();

    private Enhancer enhancer = new Enhancer();

    private boolean filterClassExist;

    private boolean interceptorClassExist;

    private GrayProperties grayProperties;

    public InterceptorBeanProcessor(GrayProperties grayProperties){
        this.grayProperties = grayProperties;
    }

    static {
        baseTypes.put("int", 0);
        baseTypes.put("short", 0);
        baseTypes.put("long", 0L);
        baseTypes.put("double", 0d);
        baseTypes.put("float", 0f);
        baseTypes.put("boolean", false);
        baseTypes.put("char", (char) 0);
        baseTypes.put("byte", Byte.valueOf((byte) 0));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        //创建filter 的代理
        Object filterProxy = createFilterProxy(bean);
        if(bean.equals(filterProxy)){
            //创建Interceptor 代理
           return createInterceptorProxy(bean);
        }else {
          return filterProxy;
        }
    }

    private Object createFilterProxy(Object bean){
        if(!filterClassExist()) {
            return bean;
        }
        if (bean instanceof Filter) {
            Class<?> clazz = bean.getClass();
            try {
                return cglibProxy(clazz, new FilterMethodInterceptor(bean,grayProperties));
            } catch (Throwable e) {
                logger.warn("create {} filter proxy failure :{}", clazz.getName(), e.getMessage());
                return bean;
            }
        }
        return bean;
    }
    private Object createInterceptorProxy(Object bean){
        if(!interceptorClassExist()) {
            return bean;
        }
        if (bean instanceof HandlerInterceptor) {
            Class<?> clazz = bean.getClass();
            try {
                return cglibProxy(clazz, new HandlerInterceptorMethodInterceptor(bean,grayProperties));
            } catch (Throwable e) {
                logger.warn("create {} interceptor proxy failure:{}", clazz.getName(), e.getMessage());
                return bean;
            }
        }
        return bean;
    }

    /**
     * 如果不是servlet容器，filter 可能不存在
     * @return
     */
    private boolean filterClassExist() {
        if(filterClassExist){
            return true;
        }
        try {
            Class clzz =  Filter.class;
            filterClassExist = true;
        }catch (Throwable e){
            filterClassExist = false;
        }
        return false;
    }

    private boolean interceptorClassExist() {
        if(interceptorClassExist){
            return true;
        }
        try {
            Class clzz =  HandlerInterceptor.class;
            interceptorClassExist = true;
        }catch ( Throwable e){
            interceptorClassExist = false;
        }
        return false;
    }

    /**
     * final 方法没法创建代理(cglib代理为实现灰的子类，故无法覆盖其final方法)
     *
     * @param clazz
     * @return
     */
    private Object cglibProxy(Class clazz, Callback callback) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(callback);
        Constructor<?>[] constructors = clazz.getConstructors();
        Parameter[] parameters = constructors[0].getParameters();
        Object[] arguments = new Object[parameters.length];
        Class[] paramsTypes = new Class[parameters.length];
        for (int i = 0; i < arguments.length; i++) {
            Class<?> type = parameters[i].getType();
            arguments[i] = baseTypes.get(type.getSimpleName().toLowerCase());
            paramsTypes[i] = type;
        }
        return enhancer.create(paramsTypes, arguments);
    }

    /**
     * 无法使用带参构造
     *
     * @param clazz
     * @return
     */
    private Object jdkProxy(Class clazz, Callback callback) {
        Class<?>[] interfaces = clazz.getInterfaces();
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, callback);
        return proxy;
    }

}
