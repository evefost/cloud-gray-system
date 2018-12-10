package com.xie.gateway.admin.vo;


import java.io.Serializable;

/**
 * 环境主机映射表
 */

public class EnvHostMapVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名称前辍
     */
    private String appPrefix;

    /**
     * 环境名称(test,test1,test2)
     */
    private String envHost;

    public String getAppPrefix() {
        return appPrefix;
    }

    public void setAppPrefix(String appPrefix) {
        this.appPrefix = appPrefix;
    }

    public String getEnvHost() {
        return envHost;
    }

    public void setEnvHost(String envHost) {
        this.envHost = envHost;
    }
}

