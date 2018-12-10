package com.xie.server.a;

import javax.servlet.*;
import java.io.IOException;

public class TestClass implements Filter {

    public void test(){
        System.out.println("TestClass.test");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public  void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("22222222222");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
