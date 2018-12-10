package com.xie.server.a;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyang on 18/12/1.
 */
@RestControllerAdvice
public class AppAdviceController {

    @ExceptionHandler
    @ResponseBody
    Map<String,String> catchEx(Exception ex){

        System.out.println("catchEx"+ex.getMessage());

        Map<String,String> map = new HashMap<>();
        map.put("catchEx",ex.getMessage());
        return map;
    }



}
