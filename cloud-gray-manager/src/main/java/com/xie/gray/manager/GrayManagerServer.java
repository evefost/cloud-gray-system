package com.xie.gray.manager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

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
