package com.xie.gray.core;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 传递灰度控制信息
 * @author xie yang
 * @date 2018/10/10-16:34
 */
public class GrayFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(Constant.ROUTE_TO_GRAY, String.valueOf(GrayUtils.isGray()));
    }
}
