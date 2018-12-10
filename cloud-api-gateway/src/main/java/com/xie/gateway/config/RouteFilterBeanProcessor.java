package com.xie.gateway.config;

import com.xie.gateway.filter.TraceNettyRoutingFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

/**
 * @author xie yang
 * @date 2018/10/29-15:59
 */
//@Component
public class RouteFilterBeanProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
        if (bean instanceof NettyRoutingFilter) {
            TraceNettyRoutingFilter filter = applicationContext
                .getBean(TraceNettyRoutingFilter.class);
            TraceNettyRoutingFilter.NettyRoutingFilterProxy proxy =
                new TraceNettyRoutingFilter.NettyRoutingFilterProxy(filter.getHttpClient(),
                    filter.getHeadersFilters());
            return proxy;
        } else {
            return bean;
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
