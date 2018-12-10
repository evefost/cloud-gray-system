package com.xie.gateway.admin.vo;

import java.util.Date;

/**
 * <p> 系统之间开放的api </p>
 *
 * @author Yanghu
 * @since 2018-08-29
 */
public class AppAuthApiVo {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 应用id(app表的)
     */
    private Integer appId;

    /**
     * 服务id应用唯一标识
     */
    private String serviceId;

    private String targetServiceId;

    /**
     * 目标应用id(app表的)
     */
    private Integer targetAppId;
    /**
     * 受限url
     */
    private String url;
    /**
     * uri作用描述
     */
    private String description;
    /**
     * 1,启用(非受权uri生效)，0禁用
     */
    private Integer enable;

    /**
     * app是否启用
     */
    private Integer appEnable;

    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;

    public String getTargetServiceId() {
        return targetServiceId;
    }

    public void setTargetServiceId(String targetServiceId) {
        this.targetServiceId = targetServiceId;
    }

    public Integer getAppEnable() {
        return appEnable;
    }

    public void setAppEnable(Integer appEnable) {
        this.appEnable = appEnable;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getTargetAppId() {
        return targetAppId;
    }

    public void setTargetAppId(Integer targetAppId) {
        this.targetAppId = targetAppId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "GatewayAppOpenApi{" + ", id=" + id + ", appId=" + appId + ", targetAppId="
            + targetAppId
            + ", url="
            + url + ", description=" + description + ", enable=" + enable + ", createdTime="
            + createdTime
            + ", updatedTime=" + updatedTime + "}";
    }
}
