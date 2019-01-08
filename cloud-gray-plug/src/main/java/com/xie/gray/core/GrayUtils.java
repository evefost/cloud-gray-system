package com.xie.gray.core;

import com.netflix.loadbalancer.Server;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xie yang
 * @date 2018/10/4-22:33
 */

public class GrayUtils {

    private static final ThreadLocal<String> routeInfo =
        new InheritableThreadLocal<String>();

    /**
     * 用于测试链路
     */
    public static final ThreadLocal<Server> currentSelectServer =
            new InheritableThreadLocal<Server>();

    public static void remove() {
        routeInfo.remove();
    }

    public static void set(String routeStatus) {
        routeInfo.set(routeStatus);
    }

    /**
     * 用于链路测度
     * @param server
     */
    public static void setTestServer(Server server){
        if(currentSelectServer.get() != null){
            currentSelectServer.set(server);
        }
    }

    public static Server getTestServer(){
        return  currentSelectServer.get();
    }

    public static void removeTestServer(){
        if(currentSelectServer.get() != null){
            currentSelectServer.remove();
        }
    }
    public static boolean isGray() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return Boolean.valueOf(routeInfo.get());
        }
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();
            Boolean aBoolean = Boolean.valueOf(request.getHeader(Constant.ROUTE_TO_GRAY));
            if (aBoolean) {
                return aBoolean;
            }
            return Boolean.valueOf(routeInfo.get());
        } else {
            return Boolean.valueOf(routeInfo.get());
        }
    }


}
