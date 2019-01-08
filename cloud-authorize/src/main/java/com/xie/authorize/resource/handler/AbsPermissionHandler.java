package com.xie.authorize.resource.handler;

import com.xie.authorize.ServiceMatcher;
import com.xie.authorize.resource.PermissionHandler;
import com.xie.gateway.api.authorize.RouteContext;
import javax.annotation.Resource;

public abstract class AbsPermissionHandler implements PermissionHandler {


    @Resource
    private ServiceMatcher seviceMatcher;

    @Override
    public boolean isSupport(RouteContext ctx) {
        return seviceMatcher.match(ctx, getServiceMatchPattern());
    }


}
