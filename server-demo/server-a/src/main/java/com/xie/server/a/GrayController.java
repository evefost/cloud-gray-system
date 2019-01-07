package com.xie.server.a;


import com.alibaba.fastjson.JSON;
import com.xie.server.a.bean.Test1;
import com.xie.server.a.bean.Test2;
import com.xie.server.a.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
public class GrayController {

    @Autowired(required = false)
    ServerCFeignService serviceCFeignService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "simple", method = RequestMethod.GET)
    public String simple() {
        if (true) {
            throw new RuntimeException("xxxxxxxxx");
        }
        return "灰度测试";

    }

    @RequestMapping(value = "simple2", method = RequestMethod.GET)
    public String simple2() {
        if (true) {
            throw new RuntimeException("出错了");
        }
        return "灰度测试2";
    }

    @RequestMapping(value = "serviceC", method = RequestMethod.GET)
    public String serviceC() {
        System.out.println("灰度测试");
        return serviceCFeignService.getGray();

    }

    @RequestMapping(value = "serviceC2", method = RequestMethod.GET)
    public ResponseEntity<String> serviceC2() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://server-c//serviceC/test/getGray", String.class, new HashMap<>());
        return forEntity;

    }

    @RequestMapping(value = "test1", method = RequestMethod.POST)
    public Test1 test1(@Validated @RequestBody Test1 test1) {
        System.out.println(JSON.toJSON(test1));
        return test1;

    }

    @RequestMapping(value = "test2", method = RequestMethod.POST)
    public Test2 test2(@Valid @RequestBody Test2 test1) {
        System.out.println(JSON.toJSON(test1));
        return test1;

    }


}
