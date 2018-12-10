package com.xie.gray.feign;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.ExtendFeignLoadBalancer;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * @author xie yang
 * @date 2018/10/31-14:01
 */
public class CachingLoadBalancerFactory extends CachingSpringLoadBalancerFactory {
    private  CachingSpringLoadBalancerFactory delegate;

    private final SpringClientFactory factory;

    private volatile Map<String, FeignLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

    private String eurekaUrl;

    public CachingLoadBalancerFactory(SpringClientFactory factory, CachingSpringLoadBalancerFactory delegate,String eurekaUrl) {
        super(null);
        this.factory = factory;
        this.delegate = delegate;
        this.eurekaUrl = eurekaUrl;
    }
    @Override
    public FeignLoadBalancer create(String clientName) {
        if (this.cache.containsKey(clientName)) {
            return this.cache.get(clientName);
        }
        IClientConfig config = this.factory.getClientConfig(clientName);
        ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
        ServerIntrospector serverIntrospector = this.factory.getInstance(clientName, ServerIntrospector.class);
        ExtendFeignLoadBalancer client =  new ExtendFeignLoadBalancer(lb, config, serverIntrospector);
        client.setEurekaUrl(eurekaUrl);
        this.cache.put(clientName, client);
        return client;
    }
}
