package com.xie.gray.test;

import java.util.List;

/**
 * Created by xieyang on 18/11/27.
 */
public class TestResult {

    private boolean success;


    private String message;

    private int total;

    private int failureCount;

    private boolean route2Gray;

    private List<ResultNode> nodeList;


    public boolean isRoute2Gray() {
        return route2Gray;
    }

    public void setRoute2Gray(boolean route2Gray) {
        this.route2Gray = route2Gray;
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

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

    public List<ResultNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<ResultNode> nodeList) {
        this.nodeList = nodeList;
    }
}
