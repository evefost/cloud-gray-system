package com.xie.server.a;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "server-c")
public interface ServerCFeignService {

    @RequestMapping(value = "/serviceC/test/getGray", method = RequestMethod.GET)
    String getGray();

    @RequestMapping(value = "/serviceC/test/queryc", method = RequestMethod.GET)
    String queryc();

    @RequestMapping(value = "/serviceC/test/queryb", method = RequestMethod.GET)
    String queryb();
}
