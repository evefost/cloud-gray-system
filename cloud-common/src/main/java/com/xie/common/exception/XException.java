package com.xie.common.exception;


public class XException extends RuntimeException {

    private String code;
    private String message;


    public XException(String message) {
        super(message);
        this.message = message;
    }

    public XException(String message, String code) {
        this(message);
        this.code = code==null?"-1":code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static XException build(String message ){
        return build(message,null);
    }

    public static XException build(String message,String code){
        return new XException(message,code);
    }
}
