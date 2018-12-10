package com.xie.gateway.util;


import com.xie.common.exception.XException;

public class BeanUtils {

    public static <T> T copy(Object source, Class<T> targetClass) {
        T target = null;
        try {
            target = targetClass.newInstance();
        } catch (Exception e) {
            throw XException.build("copy对象属性失败");
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return target;
    }
}
