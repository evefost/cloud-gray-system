package com.xie.gateway.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xie yang
 * @date 2018/11/13-11:52
 */
@Configuration
@ConfigurationProperties(prefix = "env")
public class EnvironmentProperties {

    private Map<String,String> hostMap;

    public Map<String, String> getHostMap() {
        return hostMap;
    }

    public void setHostMap(Map<String, String> hostMap) {
        this.hostMap = hostMap;
    }
}
