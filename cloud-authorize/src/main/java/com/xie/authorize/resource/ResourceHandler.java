package com.xie.authorize.resource;

/**
 * 资源权限处理
 *
 * @param <T>
 */
public interface ResourceHandler {


    /**
     * 加载用户权限资源，应用端应实现具体接口
     *
     * @param token
     * @return
     */
    void loadUserResource();


}
