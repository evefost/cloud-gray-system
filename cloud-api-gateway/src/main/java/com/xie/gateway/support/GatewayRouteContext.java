package com.xie.gateway.support;

import com.xie.gray.core.GrayRouteContext;
import org.springframework.web.server.ServerWebExchange;

import static com.xie.gray.core.Constant.SERVICE_ID;

public class GatewayRouteContext   implements GrayRouteContext<Object,ServerWebExchange> {

    private ServerWebExchange exchange;

    public GatewayRouteContext(ServerWebExchange exchange){
        this.exchange = exchange;
    }
    @Override
    public String getServiceId() {
      return (String) exchange.getAttributes().get(SERVICE_ID);
    }

    @Override
    public String getPath() {
        return exchange.getRequest().getURI().getPath();
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Object put(String key, Object object) {
        return null;
    }

    @Override
    public ServerWebExchange getExchange() {
        return exchange;
    }

    @Override
    public void setExchange(ServerWebExchange exchange) {
        this.exchange = exchange;
    }
}
