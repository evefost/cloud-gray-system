package com.xie.gateway.api.authorize;

import java.io.Serializable;

public class AuthorInfo extends AuthoMap implements Serializable, Cloneable {

    /**
     * 1,证认成功，-1客户端未注册，-2 安全码不对,-3 用户id为空
     */
    private int code = 0;

    /**
     * token
     */
    private String token;

    /**
     * 有效期时间为秒
     */
    private Integer expireInSec;

    private String scope;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        setValue("code", code);
    }

    public String getMessage() {
        return (String) get("message");
    }

    public void setMessage(String message) {
        this.message = message;
        setValue("message", message);
    }

    public String getToken() {
        return (String) get("token");
    }

    public void setToken(String token) {
        this.token = token;
        setValue("token", token);
    }

    public Integer getExpireInSec() {
        return (Integer) get("expireInSec");
    }

    public void setExpireInSec(Integer expireInSec) {
        this.expireInSec = expireInSec;
        setValue("expireInSec", expireInSec);
    }

    public String getScope() {
        return (String) get("scope");
    }

    public void setScope(String scope) {
        this.scope = scope;
        setValue("scope", scope);
    }

    public void setClientId(String clientId) {
        setValue("clientId", clientId);
    }

    public String getClientId() {
        return (String) get("clientId");

    }
}