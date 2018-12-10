package com.xie.authorize.token.jwt;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.xie.authorize.constant.Constants.TOKEN_CREATE_TIME;


@Component
public class JwtTokenProvider {

    @Autowired
    private TokenConfiguration configuration;

    /**
     * 生成token
     *
     * @return
     */
    public String createToken(Claims claims) {
        claims.setIssuer(configuration.getIss());
        claims.put(TOKEN_CREATE_TIME, System.currentTimeMillis());
        claims.setIssuedAt(new Date());
//        claims.setNotBefore(new Date());
        String compactJws = Jwts.builder().setPayload(JSONObject.toJSONString(claims))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS512, configuration.getSecretKey()).compact();
        return compactJws;
    }


    /**
     * token转换
     */
    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(configuration.getSecretKey()).parseClaimsJws(token).getBody();
    }

}
