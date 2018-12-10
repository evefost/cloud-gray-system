package com.xie.gateway.filter;


import com.xie.gray.core.GrayRouteContext;
import com.xie.gray.strategy.StrategyContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.xie.gray.core.Constant.ROUTE_CONTEXT;
import static com.xie.gray.core.Constant.ROUTE_TO_GRAY;

/**
 * 添加灰度路由请求头信息
 */
@Component
public class GrayHeaderFilter implements GlobalFilter, Ordered {

    @Autowired
    private StrategyContextFactory clientContext;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        GrayRouteContext context = (GrayRouteContext) exchange.getAttributes().get(ROUTE_CONTEXT);
        boolean gray =  clientContext.get(context).isGray(context);
        if(gray){
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpRequest newRequest = request.mutate().header(ROUTE_TO_GRAY,String.valueOf(gray)).build();
            ServerWebExchange nExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(nExchange);
        }else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
