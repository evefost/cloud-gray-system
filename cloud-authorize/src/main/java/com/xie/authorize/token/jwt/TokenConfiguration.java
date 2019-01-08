package com.xie.authorize.token.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    public SecretKeySpec getSecretKeySpec() throws UnsupportedEncodingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey
                .getBytes("utf-8"), SignatureAlgorithm.HS512.getJcaName());
        return secretKeySpec;
    }

}
