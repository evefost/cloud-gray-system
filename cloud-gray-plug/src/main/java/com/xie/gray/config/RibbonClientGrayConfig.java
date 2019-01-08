package com.xie.gray.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ServerListUpdater;
import com.xie.gray.core.GrayFeignInterceptor;
import com.xie.gray.core.GrayLoadBalancer;
import com.xie.gray.core.GrayRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author xie yang
 * @date 2018/10/10-14:16
 */

public class RibbonClientGrayConfig extends RibbonClientConfiguration {

    @Value("${ribbon.client.name}")
    private String name = "client";

    @Value("${spring.application.name}")
    private  String appName;

    @Value("${eureka.client.serviceUrl.defaultZone:}")
    private String eurekaUrls;


    @Autowired
    private PropertiesFactory propertiesFactory;

    @Bean
    @Primary
    @ConditionalOnMissingBean
    @Override
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, name)) {
            return this.propertiesFactory.get(IRule.class, config, name);
        }
        GrayRule rule = new GrayRule(appName,name);
        rule.setEurekaUrls(eurekaUrls);
        rule.initWithNiwsConfig(config);
        return rule;
    }

    @Bean
    @ConditionalOnMissingBean
    @Primary
    @Override
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
                                            ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
                                            IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        if (this.propertiesFactory.isSet(ILoadBalancer.class, name)) {
            return this.propertiesFactory.get(ILoadBalancer.class, config, name);
        }

        return new GrayLoadBalancer<>(config, rule, ping, serverList,
                serverListFilter, serverListUpdater);
    }

    @Bean
    GrayFeignInterceptor grayFeignInterceptor() {
        return new GrayFeignInterceptor();
    }


}
