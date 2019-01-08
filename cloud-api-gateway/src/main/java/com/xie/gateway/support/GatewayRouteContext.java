package com.xie.gateway.support;

import static com.xie.gray.core.Constant.SERVICE_ID;

import com.xie.gray.core.GrayRouteContext;
import java.net.InetSocketAddress;
import org.springframework.web.server.ServerWebExchange;

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

    @Override
    public String getRemoteAddr() {
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        return  remoteAddress.getAddress().getHostAddress();
    }
}
