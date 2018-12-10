package com.xie.server.c;//package com.xhg.server.b;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/test")
public class TestController {


    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "getGray", method = RequestMethod.GET)
    public String getGray() {
        return "c服务返回结果"+port;
    }
}
