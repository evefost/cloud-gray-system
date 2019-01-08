package com.xie.gateway.gray.strategy;

import static com.xie.gateway.constant.Constants.TOKEN;

import com.xie.gateway.gray.dao.TokenDao;
import com.xie.gateway.support.GatewayRouteContext;
import com.xie.gray.strategy.GrayBaseStrategy;
import com.xie.gray.strategy.GrayStrategy;
import com.xie.gray.strategy.StrategyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author xie yang
 * @date 2018/11/1-17:32
 */
public class TokenStrategy extends GrayBaseStrategy implements GrayStrategy<GatewayRouteContext> {

    @Autowired
    private TokenDao dao;

    @Override
    public boolean isGray(GatewayRouteContext context) {
        String token = context.getExchange().getRequest().getHeaders().getFirst(TOKEN);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        return dao.exist(getServiceId()+token);
    }

    @Override
    public StrategyType getType() {
        return StrategyType.TOKEN;
    }

}
