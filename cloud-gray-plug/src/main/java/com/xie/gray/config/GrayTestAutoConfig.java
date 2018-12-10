package com.xie.gray.config;

import com.xie.gray.test.GrayRouteTestController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayTestAutoConfig {

    @Bean
    @ConditionalOnMissingBean(GrayRouteTestController.class)
    public GrayRouteTestController getTestController() {
        return new GrayRouteTestController();
    }


}
