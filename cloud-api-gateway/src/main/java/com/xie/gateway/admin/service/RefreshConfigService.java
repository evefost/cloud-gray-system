package com.xie.gateway.admin.service;

import com.xie.gateway.api.event.GateWayEvent;

/**
 * 配置信息集群同步服务
 */
public interface RefreshConfigService {

    /**
     * 刷新配置
     */
    void refresh(GateWayEvent event);

}
