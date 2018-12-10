package com.xie.gateway.config;

import com.xie.gateway.gray.strategy.IpStrategy;
import com.xie.gateway.gray.strategy.TokenStrategy;
import com.xie.gateway.gray.strategy.ZoneCodeStrategy;
import com.xie.gray.strategy.impl.CompositeGrayStrategy;
import org.springframework.context.annotation.Bean;

/**
 * Created by xieyang on 18/12/6.
 */

//@Configuration
public class StrategyClientConfiguration {




    @Bean
    CompositeGrayStrategy getCompositeGrayStrategy(){
        return new CompositeGrayStrategy();
    }

    @Bean
    IpStrategy getIpStrategy(){
        return new IpStrategy();
    }

    @Bean
    TokenStrategy getTokenStrategy(){
        return new TokenStrategy();
    }

    @Bean
    ZoneCodeStrategy getZoneCodeStrategy(){
        return new ZoneCodeStrategy();
    }



}
