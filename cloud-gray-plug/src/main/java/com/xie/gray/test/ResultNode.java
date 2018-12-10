package com.xie.gray.test;

/**
 * @author xieyang
 */
public class ResultNode implements Comparable<ResultNode>{


    private boolean success=true;

    private String serviceId;

    private String host;

    private Integer port;

    private String instanceStatus="unknow";

    private int index;

    private String message ="success";

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }



    @Override
    public int compareTo(ResultNode o) {
        return o.getIndex()-this.index;
    }



}
