package com.xie.gateway.api;

public class AppInfo {

    private Integer id;

    private String serviceId;

    private OperateStatus operateStatus;

    private String isReplicate;

    /**
     * token 过期时间
     */
    private Integer tokenExpireMin;

    public Integer getTokenExpireMin() {
        return tokenExpireMin;
    }

    public void setTokenExpireMin(Integer tokenExpireMin) {
        this.tokenExpireMin = tokenExpireMin;
    }

    public String getIsReplicate() {
        return isReplicate;
    }

    public void setIsReplicate(String isReplicate) {
        this.isReplicate = isReplicate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public OperateStatus getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(OperateStatus operateStatus) {
        this.operateStatus = operateStatus;
    }
}
