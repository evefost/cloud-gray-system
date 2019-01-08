package com.xie.gateway.api.authorize;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 受权服务
 */
//@FeignClient(name="xhg-api-gateway2")
//@RequestMapping(value = "cloudgateway")
public interface AuthorizeService {

    /**
     * 认证
     *
     * @param authRequest
     * @return
     */
    @RequestMapping(value = "/oauth2/authorize", method = RequestMethod.POST, produces = "application/json")
    AuthorInfo authorize(@RequestBody AuthRequest authRequest);

    /**
     * 登出
     *
     * @param token
     * @return
     */
    @PostMapping(value = "/oauth2/logout")
    boolean logout(@RequestParam("token") String token);

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    @PostMapping(value = "/oauth2/refreshToken")
    String refreshToken(@RequestParam("token") String token);


    /**
     * 解释token，必须传clientId,及clientSecret 才能解密token
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/oauth2/parseToken")
    AuthorInfo parseToken(@RequestBody TokenParse request);


    /**
     *
     * @param token = serviceId:token
     * @return
     */
    AuthorInfo parseToken(String token);


    String getToken(String key);


}
