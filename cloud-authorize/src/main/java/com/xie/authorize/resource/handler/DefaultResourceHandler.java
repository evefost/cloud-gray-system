package com.xie.authorize.resource.handler;

import com.xie.authorize.RoutContextHolder;
import com.xie.authorize.resource.ResourceHandler;
import com.xie.authorize.resource.UserResource;
import com.xie.authorize.resource.UserResourceService;
import com.xie.gateway.api.AppManagerService;
import com.xie.gateway.api.Refresh;
import com.xie.gateway.api.authorize.RouteContext;
import com.xie.gateway.api.event.AppChangeEvent;
import com.xie.gateway.api.event.GateWayEvent;
import com.xie.gateway.api.event.RefreshEvent;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DefaultResourceHandler implements ResourceHandler, ApplicationListener<GateWayEvent>, Refresh {

    private volatile Map<String/*serviceId*/,/*permissionApi*/String> permissionApis;

    @Resource
    private AppManagerService appManagerService;

    @Resource
    protected UserResourceService userService;

    public void loadUserResource() {

        RouteContext routeContext = RoutContextHolder.getCurrentContext();
        String servcieId = routeContext.getServcieId();
        String permissionApi = permissionApis.get(servcieId);
        if (!StringUtils.isEmpty(permissionApi)) {
            UserResource userResource = userService.queryUserPermissionResource(servcieId, permissionApi);
            if (userResource != null) {
                //todo 缓存起来
            }
        }

    }


    @Override
    public void refresh() {
        permissionApis = appManagerService.loadAppPermissionApis();
    }

    @Override
    public void onApplicationEvent(GateWayEvent event) {
        if (event instanceof AppChangeEvent || event instanceof RefreshEvent) {
            refresh();
        }
    }


}
