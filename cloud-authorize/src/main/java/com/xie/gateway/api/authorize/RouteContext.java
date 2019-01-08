package com.xie.gateway.api.authorize;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;

public interface RouteContext<V> {

    String getServcieId();

    V get(Object key);

    V put(String key, Object object);

    HttpServletRequest getRequest();

    InputStream getResponseDataStream();

    void setResponseBody(String body);

    void addRequestHeader(String name, String value);

}
