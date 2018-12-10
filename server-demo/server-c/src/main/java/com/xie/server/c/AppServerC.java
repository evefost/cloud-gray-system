package com.xie.server.c;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by xieyang on 18/11/20.
 */

@SpringBootApplication
public class AppServerC {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(AppServerC.class).web(true)
                .run(args);
        System.out.printf("eureka已经启动,当前host："+applicationContext.getEnvironment().getProperty("HOSTNAME"));
    }
}
