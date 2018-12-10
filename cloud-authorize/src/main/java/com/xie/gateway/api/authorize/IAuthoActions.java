package com.xie.gateway.api.authorize;

public interface IAuthoActions {


    /**
     * 返回刷新token
     * @param newToken
     */
    void refreshToken(String newToken);


    /**
     * 提示无权限
     * @param message 提示信息
     * @param bzCode 业务码
     */
    void forbidden(String message,String bzCode);

    /**
     * 提示无权限
     * @param message 提示信息
     * @param bzCode 业务码
     * @param sysCode 系统码
     */
    void forbidden(String message,String bzCode,String sysCode);

    /**
     * 提示无权限
     * @param message 提示信息
     * @param sysMessage 系统提示信息
     * @param bzCode 业务码
     * @param sysCode 系统码
     */
    void forbidden(String message,String sysMessage,String bzCode,String sysCode);

    void forbidden(ContextMessage message);
}
