package com.xie.authorize;


import com.xie.gateway.api.authorize.RouteContext;

/**
 * 认证处理器
 */
public interface IAuthorizeHandler {

    boolean isSupport(RouteContext ctx);

    String getServiceMatchPattern();

    boolean check(RouteContext ctx);

    String getHandlerName();

}
