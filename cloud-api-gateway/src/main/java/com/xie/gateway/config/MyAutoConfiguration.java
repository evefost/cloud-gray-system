package com.xie.gateway.config;

import static org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory.REGEXP_KEY;
import static org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory.REPLACEMENT_KEY;
import static org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory.PATTERN_KEY;
import static org.springframework.cloud.gateway.support.NameUtils.normalizeFilterFactoryName;
import static org.springframework.cloud.gateway.support.NameUtils.normalizeRoutePredicateName;

import com.xie.gateway.factory.MappingPathRoutePredicateFactory;
import com.xie.gateway.factory.RewriteUrlGatewayFilterFactory;
import com.xie.gateway.filter.GrayLoadBalancerClientFilter;
import com.xie.gateway.support.RewritePathDispatcherHandler;
import com.xie.gray.strategy.StrategyContextFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * @author xie yang
 * @date 2018/10/26-17:56
 */
@Configuration
@AutoConfigureBefore({GatewayAutoConfiguration.class, WebFluxAutoConfiguration.EnableWebFluxConfiguration.class})
public class MyAutoConfiguration extends GatewayLoadBalancerClientAutoConfiguration {

    @Bean
    @ConditionalOnBean(LoadBalancerClient.class)
    @Primary
    @Override
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client) {
        return new GrayLoadBalancerClientFilter(client);
    }

    @Bean
    public RewriteUrlGatewayFilterFactory getRewriteUrlGatewayFilterFactory() {
        return new RewriteUrlGatewayFilterFactory();
    }


    @Bean
    public MappingPathRoutePredicateFactory getMappingPathRoutePredicateFactory() {
        return new MappingPathRoutePredicateFactory();
    }

    @Bean
    @Primary
    public DiscoveryLocatorProperties discoveryLocatorProperties() {
        DiscoveryLocatorProperties properties = new DiscoveryLocatorProperties();
        properties.setPredicates(initPredicates());
        properties.setFilters(initFilters());
        return properties;
    }

    public static List<PredicateDefinition> initPredicates() {
        ArrayList<PredicateDefinition> definitions = new ArrayList<>();
        // TODO: add a predicate that matches the url at /serviceId?

        // add a predicate that matches the url at /serviceId/**
        PredicateDefinition predicate = new PredicateDefinition();
        //替换PathRoutePredicateFactory
        predicate.setName(normalizeRoutePredicateName(MappingPathRoutePredicateFactory.class));
        predicate.addArg(PATTERN_KEY, "'/'+serviceId+'/**'");
        definitions.add(predicate);
        return definitions;
    }

    public static List<FilterDefinition> initFilters() {
        ArrayList<FilterDefinition> definitions = new ArrayList<>();

        // add a filter that removes /serviceId by default
        FilterDefinition filter = new FilterDefinition();
        //替换RewritePathGatewayFilterFactory
        filter.setName(normalizeFilterFactoryName(RewriteUrlGatewayFilterFactory.class));
        String regex = "'/' + serviceId + '/(?<remaining>.*)'";
        String replacement = "'/${remaining}'";
        filter.addArg(REGEXP_KEY, regex);
        filter.addArg(REPLACEMENT_KEY, replacement);
        definitions.add(filter);

        return definitions;
    }

    /**
     * 换掉原来DispatherHandler
     * @return
     */
    @Bean
    @Primary
    public DispatcherHandler webHandler() {
        return new RewritePathDispatcherHandler();
    }


    @Bean
    public StrategyContextFactory getStrategyContextFactory() {
        return new StrategyContextFactory(StrategyClientConfiguration.class);
    }



}
