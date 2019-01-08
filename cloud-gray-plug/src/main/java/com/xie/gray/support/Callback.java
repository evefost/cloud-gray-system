package com.xie.gray.support;

import java.lang.reflect.InvocationHandler;
import org.springframework.cglib.proxy.MethodInterceptor;

public interface Callback extends MethodInterceptor,InvocationHandler {
}
