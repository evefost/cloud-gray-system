package com.xie.gateway.admin.vo;

import java.io.Serializable;

/**
 * <p> <p> </p>
 *
 * @author K神带你飞
 * @since 2018-05-28
 */
public class AppVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 服务id应用唯一标识
     */
    private String serviceId;
    /**
     * 应用名称
     */
    private String name;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 版本号
     */
    private String version;
    /**
     * 1,启用服务，0禁用服务
     */
    private Integer enable;

    /**
     * 应用根路径
     */
    private String contextPath;

    /**
     * 用户权限api
     */
    private String permissionApi;

    public String getPermissionApi() {
        return permissionApi;
    }

    public void setPermissionApi(String permissionApi) {
        this.permissionApi = permissionApi;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
