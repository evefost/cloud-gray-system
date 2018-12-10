package com.xie.gray.strategy;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseDao implements GrayDao<String> {


    protected Collection<String> container;

    @Override
    public boolean exist(String params) {
        return container.contains(params);
    }

    @Override
    public  void add(String key) {
        container.add(key);
    }

    @Override
    public void delete(String params) {
        container.remove(params);
    }

    @Override
    public void clear(String serviceId){
        if(StringUtils.isEmpty(serviceId)){
            container.clear();
        }else {
            Set<String> collect = container.stream().filter(key -> {
                return !key.startsWith(serviceId);
            }).collect(Collectors.toSet());
            container = collect;
        }
    }
}
