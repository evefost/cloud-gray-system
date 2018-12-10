package com.xie.gray.strategy;

import com.xie.gray.core.GrayRouteContext;

/**
 *
 * @author xie yang
 * @date 2018/11/1-17:19
 */
public interface GrayStrategy<T extends GrayRouteContext> extends Cloneable {

    /**
     *
     * @param context
     * @return
     */
    boolean isGray(T context);


    /**
     *
     * @param serviceId
     */
    default void setServiceId(String serviceId){}

    /**
     *
     * @return
     */
    default StrategyType getType(){
        return null;
    }


}
