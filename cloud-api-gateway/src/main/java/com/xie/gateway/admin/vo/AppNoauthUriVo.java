package com.xie.gateway.admin.vo;

import java.io.Serializable;

/**
 * <p> <p> </p>
 *
 * @author K神带你飞
 * @since 2018-05-28
 */
public class AppNoauthUriVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 应用id(app表的)
     */
    private Integer appId;

    /**
     * 非受限url
     */
    private String url;

    /**
     * uri作用描述
     */
    private String description;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用服务id
     */
    private String serviceId;

    private String contextPath;

    /**
     * 1,启用(非受权uri生效)，0禁用
     */
    private Integer enable;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    @Override
    public String toString() {
        return "AppNoauthUri{" + ", id=" + id + ", appId=" + appId + ", url=" + url
            + ", description="
            + description
            + "}";
    }
}
