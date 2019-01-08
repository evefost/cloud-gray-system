package com.xie.gray.manager.bo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author xie yang
 * @date 2018/11/19-16:48
 */
public class Instance {

    @NotEmpty(message = "服务serviceId不能为空")
    private String appID;

    @NotEmpty(message = "服务instanceID不能为空")
    private String instanceID;

    private volatile String hostName;

    private volatile int port;

    private String ipAddr;

    /**
     * 上下线(UP,DOWN)
     */
    private String status;

    /**
     * 服务灰度状态
     */
    @NotEmpty(message = "服务instanceStatus不能为空")
    private String instanceStatus;

    private Boolean fromRegiestry;

    private volatile Map<String, String> metadata = new ConcurrentHashMap<String, String>();

    public String getHostName() {
        return hostName;
    }

    public Boolean getFromRegiestry() {
        return fromRegiestry;
    }

    public void setFromRegiestry(Boolean fromRegiestry) {
        this.fromRegiestry = fromRegiestry;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }
}
