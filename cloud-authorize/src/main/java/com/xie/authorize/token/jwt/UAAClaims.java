package com.xie.authorize.token.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.impl.JwtMap;

import java.util.Date;
import java.util.Map;

public class UAAClaims extends JwtMap implements Claims {

    private String[] scope;
    private String grantType = "password";
    private String deviceId;
    private String phone;
    private String clientId;
    /**
     * 单位s
     */
    private Integer expireInSec = 0;

    public UAAClaims() {

    }

    public UAAClaims(Map<String, Object> map) {
        super(map);
    }

    public Integer getExpireInSec() {
        Integer expireInSec = (Integer) get("expireInSec");
        if (expireInSec != null) {
            return expireInSec;
        }
        return 0;
    }

    public void setExpireInSec(Integer expireInSec) {
        this.expireInSec = expireInSec;
        setValue("expireInSec", this.expireInSec);
    }

    public String getClientId() {
        String clientId = (String) get("clientId");
        if (clientId != null) {
            return clientId;
        }
        return "";

    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
        setValue("clientId", this.clientId);
    }

    public String[] getScope() {
        return scope;
    }

    public void setScope(String[] scope) {
        this.scope = scope;
        setValue("scope", this.scope);
    }

    public String getGrantType() {
        String grantType = (String) get("grantType");
        if (grantType != null) {
            return grantType;
        }
        return "password";
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
        setValue("grantType", this.grantType);
    }

    public String getDeviceId() {
        return deviceId = (String) get("deviceId");
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        setValue("deviceId", this.deviceId);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        setValue("phone", this.phone);
    }

    @Override
    public String getIssuer() {
        return getString(ISSUER);
    }

    @Override
    public Claims setIssuer(String iss) {
        setValue(ISSUER, iss);
        return this;
    }

    @Override
    public String getSubject() {
        return getString(SUBJECT);
    }

    @Override
    public Claims setSubject(String sub) {
        setValue(SUBJECT, sub);
        return this;
    }

    @Override
    public String getAudience() {
        return getString(AUDIENCE);
    }

    @Override
    public Claims setAudience(String aud) {
        setValue(AUDIENCE, aud);
        return this;
    }

    @Override
    public Date getExpiration() {
        return get(Claims.EXPIRATION, Date.class);
    }

    @Override
    public Claims setExpiration(Date exp) {
        setDate(Claims.EXPIRATION, exp);
        return this;
    }

    @Override
    public Date getNotBefore() {
        return get(Claims.NOT_BEFORE, Date.class);
    }

    @Override
    public Claims setNotBefore(Date nbf) {
        setDate(Claims.NOT_BEFORE, nbf);
        return this;
    }

    @Override
    public Date getIssuedAt() {
        return get(Claims.ISSUED_AT, Date.class);
    }

    @Override
    public Claims setIssuedAt(Date iat) {
        setDate(Claims.ISSUED_AT, iat);
        return this;
    }

    @Override
    public String getId() {
        return getString(ID);
    }

    @Override
    public Claims setId(String jti) {
        setValue(Claims.ID, jti);
        return this;
    }

    @Override
    public <T> T get(String claimName, Class<T> requiredType) {
        Object value = get(claimName);
        if (value == null) {
            return null;
        }

        if (Claims.EXPIRATION.equals(claimName)
                || Claims.ISSUED_AT.equals(claimName)
                || Claims.NOT_BEFORE.equals(claimName)) {
            value = getDate(claimName);
        }

        if (requiredType == Date.class && value instanceof Long) {
            value = new Date((Long) value);
        }

        if (!requiredType.isInstance(value)) {
            throw new RequiredTypeException("Expected value to be of type: "
                    + requiredType + ", but was " + value.getClass());
        }

        return requiredType.cast(value);
    }

}
