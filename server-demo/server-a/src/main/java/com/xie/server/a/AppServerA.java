package com.xie.server.a;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xieyang on 18/11/20.
 */

@SpringBootApplication
//@EnableFeignClients(basePackages = "com.xie")
public class AppServerA {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(AppServerA.class).web(true)
                .run(args);
        System.out.printf("eureka已经启动,当前host："+applicationContext.getEnvironment().getProperty("HOSTNAME"));
    }

    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    @Order(2)
    RewritePathFilter2 getRewritePathFilter2(){
        return new RewritePathFilter2(2);
    }
}
