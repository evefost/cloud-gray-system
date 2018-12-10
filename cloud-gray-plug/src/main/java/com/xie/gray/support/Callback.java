package com.xie.gray.support;

import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;

public interface Callback extends MethodInterceptor,InvocationHandler {
}
