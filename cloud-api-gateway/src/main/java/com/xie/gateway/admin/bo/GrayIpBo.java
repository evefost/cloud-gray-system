package com.xie.gateway.admin.bo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author xie yang
 * @date 2018/11/2-14:59
 */
public class GrayIpBo {


    @NotEmpty(message = "服务serviceId不能为空")
    private String serviceId;

    @NotEmpty(message = "服务ip不能为空")
    @Pattern(regexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}", message = "ip格式不对")
    private String ip;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
