package com.xie.authorize.token.handler;

import com.xie.gateway.api.authorize.RouteContext;
import org.springframework.stereotype.Component;


/**
 * 端登录处理
 */
@Component
public class DefaultTokenHanlder extends AbsTokenHanlder {

    @Override
    public String getServiceMatchPattern() {
        return null;
    }



    @Override
    public String parseToken(RouteContext ctx) {
        return super.parseTokenDefault(ctx);
    }

    @Override
    public boolean isCreateToken() {
        return false;
    }

    @Override
    public int order() {
        return 1;
    }
    @Override
    public String getHandlerName() {
        return "default_token_handler";
    }

}
