package com.xie.gateway.api;

public class UriInfo {


    private Integer appId;

    private String serviceId;

    private String url;

    private String isReplicate;

    public String getIsReplicate() {
        return isReplicate;
    }

    public void setIsReplicate(String isReplicate) {
        this.isReplicate = isReplicate;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    private OperateStatus operateStatus;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OperateStatus getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(OperateStatus operateStatus) {
        this.operateStatus = operateStatus;
    }
}
