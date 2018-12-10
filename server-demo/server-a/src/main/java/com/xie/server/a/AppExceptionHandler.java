package com.xie.server.a;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/7/5.
 */

@ControllerAdvice
public class AppExceptionHandler {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 处理校验或其它异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handValidationError( Exception e) {
        logger.error("出错了:",e);
        ResponseVo responseDataVo = ResponseVo.failure(e.getMessage());
        return responseDataVo;
    }
}
