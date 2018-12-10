package com.xie.gray.manager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

/**
 * @author xie
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.xie.**.remote")
public class GrayManagerServer {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(GrayManagerServer.class).web(true)
                .run(args);
        System.out.printf("eureka已经启动,当前host：" + applicationContext.getEnvironment().getProperty("HOSTNAME"));
    }


}
