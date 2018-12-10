package com.xie.gateway.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.xie.common.exception.XException;
import com.xie.common.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author xie yang
 * @date 2018/10/26-9:47
 */
public class GlobalWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public GlobalWebExceptionHandler(ErrorAttributes errorAttributes,
        ResourceProperties resourceProperties,
        ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    protected Mono<ServerResponse> renderErrorView(ServerRequest request) {
        return this.renderErrorResponse(request);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        boolean includeStackTrace = isIncludeStackTrace(request, MediaType.ALL);
        Throwable throwable = this.getError(request);
        Map<String, Object> error = getErrorAttributes(request, includeStackTrace);
        HttpStatus errorStatus = getHttpStatus(error);
        ResponseVo responseDataVo = handValidationError(throwable, request, errorStatus);
        return ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(BodyInserters.fromObject(responseDataVo))
            .doOnNext((resp) -> logError(request, errorStatus));
    }

    public ResponseVo handValidationError(Throwable e, ServerRequest request,
                                          HttpStatus errorStatus) {
        String path = request.path();
        ResponseVo responseVo = ResponseVo.failure();
        if (404 == errorStatus.value()) {
            responseVo.setMessage("资源不存在或服务没启动");
        } else if (e instanceof XException) {
            XException xhgException = (XException) e;
            logger.warn("处理异常:{} code:{}", xhgException.getMessage(), xhgException.getCode());
            responseVo.setCode(xhgException.getCode());
            responseVo.setMessage(xhgException.getMessage());
        } else if (e instanceof BindException) {

            BindException bindException = (BindException) e;
            String message = bindException.getAllErrors().get(0).getDefaultMessage();
            logger.error("参数校验异常:{}", message);
            responseVo.setMessage(message);
        } else if (e instanceof MethodNotAllowedException) {
            String method = ((MethodNotAllowedException) e).getHttpMethod();
            logger.warn("不支持" + method + "请求");
            responseVo.setMessage("不支持" + method + "请求");
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException arEx = (MethodArgumentNotValidException) e;
            String params = arEx.getBindingResult().getTarget().toString();
            logger.info("参数有误params:{}", params);
            String defaultMessage = arEx.getBindingResult().getAllErrors().get(0)
                .getDefaultMessage();
            responseVo.setMessage(defaultMessage);
        } else if (e instanceof InvalidFormatException) {
            responseVo.setMessage("无效的格式");
        } else if (e instanceof JsonParseException) {
            responseVo.setMessage("json格式有误");
        } else if (e instanceof WebExchangeBindException) {
            WebExchangeBindException exchangeBindException = (WebExchangeBindException) e;
            List<ObjectError> allErrors = exchangeBindException.getBindingResult().getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String defaultMessage = objectError.getDefaultMessage();
            responseVo.setMessage(defaultMessage);
        } else if (e instanceof ServerWebInputException) {
            responseVo.setMessage("输入参数不正确");
        } else if (e.getCause() != null && e.getCause() instanceof ConnectException) {
            responseVo.setMessage("连接远程服务异常");
//        } else if (e instanceof NotFoundException) {
//            rb.setMessage("服务没实例");
        } else {
            logger.error("系统出错", e);
            responseVo.setCode("500");
            responseVo.setMessage("网关服务出错");
        }
        if("500".equals(responseVo.getCode())){
          logger.error("网关系统出错:",e);
        }else {
            logger.error("业务异常:{}",responseVo.getMessage());
        }
        return responseVo;
    }
}
