package com.xie.gateway.admin.bo;

import java.io.Serializable;
import java.util.List;

/**
 * <p> <p> </p>
 *
 * @author K神带你飞
 * @since 2018-05-28
 */
public class AppUrisBo implements Serializable {

    /**
     * 应用id(app表的)
     */
    //    @NotNull(message = "应用id不能为空")
    private Integer appId;

    //    @NotNull(message = "应用id不能为空")
    private String serviceId;

    /**
     * 非受权uri
     */
    //    @NotEmpty(message = "urls不能为空")
    private List<String> urlList;

    private String urls;

    /**
     * uri作用描述
     */
    private String description;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
