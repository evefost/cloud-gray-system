package com.xie.gray.strategy;

/**
 * @author xie yang
 * @date 2018/11/5-15:21
 */
public abstract class GrayBaseStrategy {

    protected String serviceId;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
