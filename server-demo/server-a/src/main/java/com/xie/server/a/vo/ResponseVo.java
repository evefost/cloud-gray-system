package com.xie.server.a.vo;

public class ResponseVo<T> {

    private int code;

    private String message;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    private ResponseVo(T data, int code, String message){
        this.data =data;
        this.code = code;
        this.message = message;
    }
    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseVo<T> success(T data){
        return new ResponseVo(data,1,"成功");
    }
    public static <T> ResponseVo<T> failure(){
        return new ResponseVo(null,-1,"失败");
    }

    public static <T> ResponseVo<T> failure(String message){
        return new ResponseVo(null,-1,message);
    }

}
