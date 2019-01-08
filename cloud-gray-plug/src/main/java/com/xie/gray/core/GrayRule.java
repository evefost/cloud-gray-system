package com.xie.gray.core;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.PredicateBasedRule;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author xie yang
 * @date 2018/10/10-14:19
 */
public class GrayRule extends PredicateBasedRule {

    final static Logger logger = LoggerFactory.getLogger(GrayRule.class);

    private AbstractServerPredicate predicate = new GrayPredicate();

    private RoundRobinRule roundRobinRule;

    private String appName;

    private String clientName;

    private String eurekaUrls;

    public void setEurekaUrls(String eurekaUrls) {
        this.eurekaUrls = eurekaUrls;
    }

    public GrayRule() {

    }

    public GrayRule(String appName, String clientName) {
        this.appName = appName;
        this.clientName = clientName;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
        roundRobinRule = new RoundRobinRule();
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        roundRobinRule.setLoadBalancer(lb);
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return predicate;
    }

    @Override
    public Server choose(Object key) {
        boolean gray = GrayUtils.isGray();
        String routeStatus = gray ? "灰度服务" : "正常服务";
        logger.debug("[{}]将路由到[{}]的{}", appName, clientName, routeStatus);
        Server server = super.choose(gray ? ServerInstanceStatus.GRAY : ServerInstanceStatus.NORMAL);
        if (gray && server == null) {
            logger.debug("[{}]没有灰度服务实例，回退路由到正常服务", clientName);
            server = super.choose(ServerInstanceStatus.NORMAL);
        }
        if (server == null) {
            logger.error("[{}]没有{}实例,请检查服务环境当前注册中心:{}", clientName, routeStatus, eurekaUrls);
        } else {
            logger.debug("当前选择的服务[{}]:{}", clientName, server.getHostPort());
            GrayUtils.setTestServer(server);
        }
        return server;
    }

}
