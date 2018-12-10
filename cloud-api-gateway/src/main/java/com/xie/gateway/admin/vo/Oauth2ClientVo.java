package com.xie.gateway.admin.vo;

import java.io.Serializable;

/**
 * <p> 客户端认证信息(只有注册的应用才能被认证) </p>
 *
 * @author K神带你飞
 * @since 2018-05-30
 */
public class Oauth2ClientVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端id
     */
    private Integer id;

    /**
     * 客户端名称
     */
    private String clientName;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端安全码
     */
    private String clientSecret;

    /**
     * 服务id应用唯一标识
     */
    private String serviceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
