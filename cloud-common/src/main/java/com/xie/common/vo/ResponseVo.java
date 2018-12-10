package com.xie.common.vo;


import java.io.Serializable;

public class ResponseVo<T> implements Serializable, Cloneable {

    public static final long serialVersionUID = 6334971409410600125L;

    private T data;

    private String code = "1";

    private String message = "成功";

    private ResponseVo(String message) {
        this.message = message == null ? "" : message;
    }

    private ResponseVo(String message, String code, T data) {
        this(message);
        this.code = code == null ? "-1" : code;
        this.data = data == null ? null : data;
    }

    public static <T> ResponseVo<T> success() {
        return success(null);
    }

    public static <T> ResponseVo<T> success(String message) {
        return success(message, null);
    }

    public static <T> ResponseVo<T> success(T data) {
        return success(null, null, data);
    }

    public static <T> ResponseVo<T> success(String message, String code) {
        return success(message, code, null);
    }

    public static <T> ResponseVo<T> success(String message, String code, T data) {
        ResponseVo<T> responseVo = new ResponseVo(message, code, data);
        return responseVo;
    }

    public static <T> ResponseVo<T> failure() {
        return failure(null);
    }
    public static <T> ResponseVo<T> failure(String message) {
        return failure(message, null);
    }

    public static <T> ResponseVo<T> failure(String message, String code) {
        ResponseVo<T> responseVo = new ResponseVo(message);
        responseVo.code = code == null ? "-1" : code;
        return responseVo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
