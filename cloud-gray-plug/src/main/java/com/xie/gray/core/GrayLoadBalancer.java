package com.xie.gray.core;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ServerListUpdater;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

/**
 * @author xie yang
 * @date 2018/10/9-10:41
 */
public class GrayLoadBalancer<T extends Server> extends ZoneAwareLoadBalancer {



    public GrayLoadBalancer(IClientConfig config, IRule rule, IPing ping, ServerList<T> serverList, ServerListFilter<T> serverListFilter, ServerListUpdater serverListUpdater) {
        super(config,rule,ping,serverList,serverListFilter,serverListUpdater);
    }

    @Override
    public Server chooseServer(Object key){
        Server server = super.chooseServer(key);
        return server;
    }


}
