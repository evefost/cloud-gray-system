/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xie.gateway.filter;

import static com.xie.gray.core.Constant.ROUTE_TO_GRAY;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import com.xie.common.exception.XException;
import com.xie.gray.core.GrayUtils;
import java.net.URI;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Spencer Gibb
 * @author Tim Ysewyn
 */
public class GrayLoadBalancerClientFilter extends LoadBalancerClientFilter {

    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10100;
    final static Logger logger = LoggerFactory.getLogger(GrayLoadBalancerClientFilter.class);
    private final LoadBalancerClient loadBalancer;

    public GrayLoadBalancerClientFilter(LoadBalancerClient loadBalancer) {
        super(loadBalancer);
        this.loadBalancer = loadBalancer;
    }

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        //preserve the original url
        addOriginalRequestUrl(exchange, url);

        final ServiceInstance instance;
        String routeTo = exchange.getRequest().getHeaders().getFirst(ROUTE_TO_GRAY);
        try {
            if (!StringUtils.isEmpty(routeTo)) {
                GrayUtils.set(routeTo);
            }
            instance = loadBalancer.choose(url.getHost());
        } finally {
            GrayUtils.remove();
        }
        if (instance == null) {
            throw XException.build("无发现服务实例" + url.getHost());
        }
        logger.debug("current select server[{}]{}:{}", instance.getServiceId(), instance.getHost(),
            instance.getPort());
        URI uri = exchange.getRequest().getURI();

        // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
        // if the loadbalancer doesn't provide one.
        String overrideScheme = null;
        if (schemePrefix != null) {
            overrideScheme = url.getScheme();
        }

        URI requestUrl = loadBalancer
            .reconstructURI(new DelegatingServiceInstance(instance, overrideScheme), uri);

        logger.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
        return chain.filter(exchange);
    }

    class DelegatingServiceInstance implements ServiceInstance {

        final ServiceInstance delegate;
        private String overrideScheme;

        DelegatingServiceInstance(ServiceInstance delegate, String overrideScheme) {
            this.delegate = delegate;
            this.overrideScheme = overrideScheme;
        }

        @Override
        public String getServiceId() {
            return delegate.getServiceId();
        }

        @Override
        public String getHost() {
            return delegate.getHost();
        }

        @Override
        public int getPort() {
            return delegate.getPort();
        }

        @Override
        public boolean isSecure() {
            return delegate.isSecure();
        }

        @Override
        public URI getUri() {
            return delegate.getUri();
        }

        @Override
        public Map<String, String> getMetadata() {
            return delegate.getMetadata();
        }

        @Override
        public String getScheme() {
            String scheme = delegate.getScheme();
            if (scheme != null) {
                return scheme;
            }
            return this.overrideScheme;
        }

    }
}
