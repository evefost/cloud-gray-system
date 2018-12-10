package com.xie.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 资源权限验证
 */
@Component
public class TokenValidataFilter implements GlobalFilter, Ordered {

    protected static final Logger logger = LoggerFactory.getLogger(TokenValidataFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.debug("自定义过滤器");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
