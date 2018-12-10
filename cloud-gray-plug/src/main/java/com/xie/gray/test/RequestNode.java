package com.xie.gray.test;

/**
 * @author xieyang
 */
public class RequestNode {

    private int index;

    private String serviceId;

    private RequestNode next;

    public RequestNode() {
    }

    public RequestNode(String serviceId, int index) {
        this.serviceId = serviceId;
        this.index = index;
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

    public RequestNode getNext() {
        return next;
    }

    public void setNext(RequestNode next) {
        this.next = next;
    }


}
