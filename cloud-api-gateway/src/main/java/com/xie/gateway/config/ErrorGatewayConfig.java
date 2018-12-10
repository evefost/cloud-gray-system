package com.xie.gateway.config;

import com.xie.gateway.filter.GrayLoadBalancerClientFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * @author xie yang
 * @date 2018/10/26-9:50
 */
@Configuration
public class ErrorGatewayConfig extends ErrorWebFluxAutoConfiguration {

    private final ServerProperties serverProperties;

    private final ApplicationContext applicationContext;

    private final ResourceProperties resourceProperties;

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    public ErrorGatewayConfig(ServerProperties serverProperties, ResourceProperties resourceProperties,
                              ObjectProvider<List<ViewResolver>> viewResolversProvider,
                              ServerCodecConfigurer serverCodecConfigurer,
                              ApplicationContext applicationContext) {
        super(serverProperties, resourceProperties, viewResolversProvider, serverCodecConfigurer,
            applicationContext);
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolversProvider.getIfAvailable(() -> Collections.emptyList());
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorWebExceptionHandler.class, search = SearchStrategy.CURRENT)
    @Order(-1)
    @Primary
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
        DefaultErrorWebExceptionHandler exceptionHandler =
            new GlobalWebExceptionHandler(errorAttributes, this.resourceProperties,
                this.serverProperties.getError(),
                this.applicationContext);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

    @Bean
    @ConditionalOnBean(LoadBalancerClient.class)
    @Primary
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client) {
        return new GrayLoadBalancerClientFilter(client);
    }
}
