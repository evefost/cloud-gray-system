package com.xie.gateway.admin.controller;

//import com.xhg.gateway.admin.LoginManager;

import com.xie.common.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用管理
 */
@RestController
@RequestMapping("/admin")
public class AdminController
    extends BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    //    @Resource
    //    private LoginManager loginManager;

    @Value("${admin.username:}")
    private String username;

    @Value("${admin.password:}")
    private String password;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseVo login(String username, String password) throws InterruptedException {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResponseVo.failure("帐号密码不对");
        }
        //        if(username.equals(this.username)&& password.equals(this.password)){
        //            String token = loginManager.login();
        //            Map<String,String> result = new HashMap<>();
        //            result.put("token",token);
        //           return ResponseBean.success(result);
        //        }
        return ResponseVo.failure("帐号密码不对");
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseVo logout() {
        //LoginManager.logout();
        return ResponseVo.success();
    }

}

