package com.xie.gray.core;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

/**
 * 灰度判断处理
 *
 * @author xie yang
 * @date 2018/10/10-15:26
 */
public class GrayPredicate extends AbstractServerPredicate {


    @Override
    public boolean apply(PredicateKey input) {
        Server server = input.getServer();
        ServerInstanceStatus routTo = (ServerInstanceStatus) input.getLoadBalancerKey();
        if (server instanceof DiscoveryEnabledServer) {
            DiscoveryEnabledServer enabledServer = (DiscoveryEnabledServer) server;
            InstanceInfo instanceInfo = enabledServer.getInstanceInfo();
            String serverStatus = instanceInfo.getMetadata().get(Constant.INSTANCE_STATUS);
            switch (routTo) {
                case GRAY:
                    return ServerInstanceStatus.GRAY.getValue().equals(serverStatus);
                case NORMAL:
                    if (ServerInstanceStatus.GRAY.getValue().equals(serverStatus) || ServerInstanceStatus.DISABLE.getValue().equals(serverStatus)) {
                        return false;
                    }
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
