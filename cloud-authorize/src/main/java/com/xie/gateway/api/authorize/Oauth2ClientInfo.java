package com.xie.gateway.api.authorize;

/**
 * <p>
 * 客户端认证信息(只有注册的应用才能被认证)
 * </p>
 *
 * @author Yanghu
 * @since 2018-08-09
 */

public class Oauth2ClientInfo {


    /**
     * 客户端id
     */
    private Long id;
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
     * 应用id(app表的)
     */
    private Integer appId;
    /**
     * 重定向uri
     */
    private String redirectUri;
    /**
     * 客户端uri
     */
    private String clientUri;

    /**
     * 授权范围
     */
    private String scope;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
