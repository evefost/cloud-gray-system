package com.xie.gateway.api.authorize;

/**
 * @author xie yang
 * @date 2018/9/6-10:38
 */
public class ContextMessage {

    /**
     * 系统信息
     */
    private String sysMessage;

    /**
     * 系统码
     */
    private String sysCode;

    /**
     * 业务信息
     */
    private String bzMessage;

    /**
     * 业务码
     */
    private String bzCode;

    public String getSysMessage() {
        return sysMessage;
    }

    public void setSysMessage(String sysMessage) {
        this.sysMessage = sysMessage;
    }

    public String getBzMessage() {
        return bzMessage;
    }

    public void setBzMessage(String bzMessage) {
        this.bzMessage = bzMessage;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getBzCode() {
        return bzCode;
    }

    public void setBzCode(String bzCode) {
        this.bzCode = bzCode;
    }
}
