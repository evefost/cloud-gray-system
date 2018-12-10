package com.xie.authorize;

import com.xie.gateway.api.authorize.RouteContext;

public class RoutContextHolder {

    private static final ThreadLocal<RouteContext> ROUTE_CONTEXT_THREAD_LOCAL =
            new ThreadLocal<>();

    public static void set(RouteContext context) {
        ROUTE_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static RouteContext getCurrentContext() {
        return ROUTE_CONTEXT_THREAD_LOCAL.get();
    }

    public static void remove() {
        ROUTE_CONTEXT_THREAD_LOCAL.remove();
    }
}
