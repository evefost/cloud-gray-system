package com.xie.gray.strategy.impl;


import com.xie.gray.core.GrayRouteContext;
import com.xie.gray.strategy.GrayBaseStrategy;
import com.xie.gray.strategy.GrayStrategy;
import com.xie.gray.strategy.ICompositeGray;
import com.xie.gray.strategy.StrategyType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.xie.gray.strategy.StrategyType.COMPOSITE;

/**
 * 组合灰度路由策略
 *
 * @author xie yang
 * @date 2018/11/1-17:26
 */

public class CompositeGrayStrategy extends GrayBaseStrategy implements ICompositeGray<GrayRouteContext> {

    private volatile Map<StrategyType, GrayStrategy> grayStrategies = new HashMap<>();

    public CompositeGrayStrategy() {
    }

    @Override
    public boolean isGray(GrayRouteContext t) {
        if (grayStrategies.isEmpty()) {
            return false;
        }
        Collection<GrayStrategy> values = grayStrategies.values();
        for (GrayStrategy strategy : values) {
            if (strategy.isGray(t)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public StrategyType getType() {
        return COMPOSITE;
    }



    @Override
    public void add(GrayStrategy strategy) {
        grayStrategies.put(strategy.getType(), strategy);
    }


}
