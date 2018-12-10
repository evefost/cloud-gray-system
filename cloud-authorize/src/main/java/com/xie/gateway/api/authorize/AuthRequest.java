package com.xie.gateway.api.authorize;

import java.util.Map;

import static com.sun.corba.se.impl.util.Version.PROJECT_NAME;

public class AuthRequest extends AuthoMap {


    public AuthRequest() {
        super();
    }

    public AuthRequest(Map<String, Object> map) {
        super(map);
    }

    public AuthRequest(String clientId, String clientSecret) {
        this();

        this.setClientId(clientId);
        this.setClientSecret(clientSecret);
    }

    public AuthRequest(String clientId, String clientSecret, String userId) {
        this();
        this.setClientId(clientId);
        this.setClientSecret(clientSecret);
        this.setUserId(userId);
    }


    /**
     * 用户id {@link AuthRequest#getResponseType()}等于token是不能为空
     *
     * @return
     */
    public String getUserId() {
        return (String) get("userId");
    }

    public void setUserId(String userId) {
        setValue("userId", userId);
    }

    public String getProjectName() {
        return (String) get(PROJECT_NAME);
    }

    public void setProjectName(String projectName) {
        setValue(PROJECT_NAME, projectName);
    }

    /**
     * 客户端id(相对于网关)
     *
     * @return
     */
    public String getClientId() {
        return (String) get("clientId");

    }

    public int getExpireInSec() {
        Object expireInSec = get("expireInSec");
        if (expireInSec != null) {
            return (int) expireInSec;
        }
        return 0;
    }

    public void setExpireInSec(int expireInSec) {
        setValue("expireInSec", expireInSec);
    }

    public void setClientId(String clientId) {
        setValue("clientId", clientId);
    }


    public String getClientSecret() {
        return (String) get("clientSecret");
    }

    public void setClientSecret(String clientSecret) {
        setValue("clientId", clientSecret);
    }

    public String getResponseType() {
        return (String) get("responseType");
    }

    /**
     * 认证类型{@link ResponseType}
     *
     * @param responseType
     */
    public void setResponseType(String responseType) {
        setValue("responseType", responseType);
    }
}
