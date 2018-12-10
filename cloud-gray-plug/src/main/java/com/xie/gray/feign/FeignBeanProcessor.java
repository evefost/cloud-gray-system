package com.xie.gray.feign;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author xie yang
 * @date 2018/10/29-15:59
 */
@Component
public class FeignBeanProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Value("${eureka.client.serviceUrl.defaultZone:}")
    private String eurekaUrls;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
        if (bean instanceof CachingSpringLoadBalancerFactory) {
           return new CachingLoadBalancerFactory(applicationContext.getBean(SpringClientFactory.class),
               (CachingSpringLoadBalancerFactory) bean,eurekaUrls);
        } else {
            return bean;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
