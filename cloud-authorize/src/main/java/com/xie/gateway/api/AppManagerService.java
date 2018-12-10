package com.xie.gateway.api;

import java.util.List;
import java.util.Map;

public interface AppManagerService {

    /**
     * 禁用的服务列表
     *
     * @return
     */
    List</*serviceId*/String> disableServiceList();

    /**
     * 无需受权服务列表
     *
     * @return
     */
    List</*serviceId*/String> noAuthoServiceList();

    /**
     * 非受权资源列表
     * key:serviceId,value：uris
     *
     * @return
     */
    Map</*serviceId*/String,/*uris*/List<String>> noAuthUriList();

    Map</*serviceId*/String,/*permissionApi*/String> loadAppPermissionApis();


    /**
     * 据服务serviceId 获取服信息
     *
     * @param serviceId
     * @return
     */
    AppInfo queryAppInfoByServiceId(String serviceId);


}
