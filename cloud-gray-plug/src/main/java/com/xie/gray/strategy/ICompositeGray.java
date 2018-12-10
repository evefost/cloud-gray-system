package com.xie.gray.strategy;

import com.xie.gray.core.GrayRouteContext;

/**
 * @author xie yang
 * @date 2018/11/2-14:33
 */
public interface ICompositeGray<T extends GrayRouteContext> extends GrayStrategy<T> {


    /**
     *
     * @param strategy
     */
    void add(GrayStrategy strategy);
}
