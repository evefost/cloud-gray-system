package com.xie.gray.strategy;

import com.xie.gray.core.GrayRouteContext;

/**
 *
 * @author xie yang
 * @date 2018/11/1-17:19
 */
public interface GrayStrategy<T extends GrayRouteContext> extends Cloneable {

    /**
     *  请求 context 信息
     * @param context
     * @return
     */
    boolean isGray(T context);

    /**
     *  策略所有属的服务
     * @param serviceId
     */
    default void setServiceId(String serviceId){}

    /**
     *  策略类型
     * @return
     */
    default StrategyType getType(){
        return null;
    }
}
