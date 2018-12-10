package com.xie.gray.strategy;

/**
 * Created by xieyang on 18/12/6.
 */
public interface GrayDao<P> {

    boolean exist(P params);

    default void clear(String serviceId) {
    }

    default void add(P params) {

    }

    default void delete(P params) {

    }
}
