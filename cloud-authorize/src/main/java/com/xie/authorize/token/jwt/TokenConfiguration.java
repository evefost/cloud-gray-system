package com.xie.authorize.token.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
@ConfigurationProperties("token")
public class TokenConfiguration {

    private String iss;

    /**
     * token生成的secretkey
     */
    private String secretKey;

    /**
     * 是否允许跨服务访问
     */
    private boolean accessOther=true;


    public boolean isAccessOther() {
        return accessOther;
    }

    public void setAccessOther(boolean accessOther) {
        this.accessOther = accessOther;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public SecretKeySpec getSecretKeySpec() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey
                .getBytes(), SignatureAlgorithm.HS512.getJcaName());
        return secretKeySpec;
    }

}
