package com.xie.gateway.api.authorize;

public enum ResponseType {
    CODE("code"),
    TOKEN("token"),
    CLIENT("client");
    private String code;

    private ResponseType(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code;
    }

    public String value() {
        return code;
    }
}