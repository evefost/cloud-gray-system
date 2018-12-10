package com.xie.gateway.gray.strategy;

import com.xie.gateway.gray.bo.ZoneBo;
import com.xie.gateway.gray.dao.ZoneCodeDao;
import com.xie.gateway.support.GatewayRouteContext;
import com.xie.gray.strategy.GrayBaseStrategy;
import com.xie.gray.strategy.GrayStrategy;
import com.xie.gray.strategy.StrategyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import static com.xie.gateway.constant.Constants.ZONECODE;

/**
 * @author xie yang
 * @date 2018/11/1-17:32
 */
public class ZoneCodeStrategy extends GrayBaseStrategy implements GrayStrategy<GatewayRouteContext> {

    @Autowired
    private ZoneCodeDao dao;

    @Override
    public boolean isGray(GatewayRouteContext context) {
        String zoneCode = context.getExchange().getRequest().getHeaders().getFirst(ZONECODE);
        if (StringUtils.isEmpty(zoneCode)) {
            return false;
        }
        ZoneBo bo = new ZoneBo();
        bo.setServiceId(getServiceId());
        bo.setZoneCode(zoneCode);
        return dao.exist(bo);
    }

    @Override
    public StrategyType getType() {
        return StrategyType.ZONE_CODE;
    }



}
