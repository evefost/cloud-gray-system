package com.xie.gray.core;

/**
 * 路由context
 *
 * @param <V>
 * @param <E>
 */
public interface GrayRouteContext<V, E>  {

    String getServiceId();

    String getPath();

    V get(Object key);

    V put(String key, Object object);

    E getExchange();

    void setExchange(E exchange);

    String getRemoteAddr();
}
