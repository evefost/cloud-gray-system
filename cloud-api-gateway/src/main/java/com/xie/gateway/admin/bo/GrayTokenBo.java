package com.xie.gateway.admin.bo;

import javax.validation.constraints.NotEmpty;

/**
 * @author xie yang
 * @date 2018/11/2-14:59
 */
public class GrayTokenBo {



    @NotEmpty(message = "服务serviceId不能为空")
    private String serviceId;

    @NotEmpty(message = "token不能为空")
    private String token;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
