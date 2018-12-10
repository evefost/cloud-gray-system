package com.xie.authorize.token;

import com.xie.authorize.IAuthorizeHandler;
import com.xie.gateway.api.authorize.RouteContext;

public interface TokenHandler<T> extends IAuthorizeHandler {

    String parseTokenDefault(RouteContext ctx);

    T parseToken(RouteContext ctx);

    boolean isCreateToken();

    void createToken(RouteContext ctx);

    void setNextHandler(TokenHandler next);

    boolean findLoginInfo(RouteContext ctx);


    int order();
}