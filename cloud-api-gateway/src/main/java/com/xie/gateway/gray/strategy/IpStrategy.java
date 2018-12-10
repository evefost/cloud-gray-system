package com.xie.gateway.gray.strategy;

import com.xie.gateway.gray.dao.IpDao;
import com.xie.gateway.support.GatewayRouteContext;
import com.xie.gray.strategy.GrayBaseStrategy;
import com.xie.gray.strategy.GrayStrategy;
import com.xie.gray.strategy.StrategyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;

/**
 * @author xie yang
 * @date 2018/11/1-17:32
 */
public class IpStrategy extends GrayBaseStrategy implements GrayStrategy<GatewayRouteContext> {

    @Autowired
    private IpDao dao;

    @Override
    public boolean isGray(GatewayRouteContext context) {
        ServerWebExchange exchange = context.getExchange();
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        String ip = remoteAddress.getAddress().getHostAddress();
        return dao.exist(getServiceId()+ip);
    }


    @Override
    public StrategyType getType() {
        return StrategyType.IP;
    }





}
