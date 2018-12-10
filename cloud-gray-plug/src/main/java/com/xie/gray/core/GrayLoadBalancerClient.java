package com.xie.gray.core;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.netflix.ribbon.*;

import java.io.IOException;

public class GrayLoadBalancerClient extends RibbonLoadBalancerClient {

    final static Logger logger = LoggerFactory.getLogger(GrayLoadBalancerClient.class);

    private SpringClientFactory clientFactory;

    public GrayLoadBalancerClient(SpringClientFactory clientFactory) {
        super(clientFactory);
        this.clientFactory = clientFactory;
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
        ILoadBalancer loadBalancer = getLoadBalancer(serviceId);
        Server server = getServer(loadBalancer);
        if (server == null) {
            throw new RuntimeException(serviceId + "没有实例");
        }
        RibbonServer ribbonServer = new RibbonServer(serviceId, server, isSecure(server,
                serviceId), serverIntrospector(serviceId).getMetadata(server));

        return execute(serviceId, ribbonServer, request);
    }

    private boolean isSecure(Server server, String serviceId) {
        IClientConfig config = this.clientFactory.getClientConfig(serviceId);
        ServerIntrospector serverIntrospector = serverIntrospector(serviceId);
        return RibbonUtils.isSecure(config, serverIntrospector, server);
    }

    private ServerIntrospector serverIntrospector(String serviceId) {
        ServerIntrospector serverIntrospector = this.clientFactory.getInstance(serviceId,
                ServerIntrospector.class);
        if (serverIntrospector == null) {
            serverIntrospector = new DefaultServerIntrospector();
        }
        return serverIntrospector;
    }

}
