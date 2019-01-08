package com.xie.authorize;

import com.xie.gateway.api.authorize.RouteContext;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ServiceMatcher {

    public boolean match(RouteContext ctx, String serviceMatchPattern) {
        String serviceId = ctx.getServcieId();
        if (serviceId == null) {
            return false;
        }
        if (serviceMatchPattern == null) {
            return false;
        }
        return Pattern.matches(serviceMatchPattern, serviceId);
    }
}
