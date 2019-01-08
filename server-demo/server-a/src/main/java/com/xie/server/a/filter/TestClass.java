package com.xie.server.a.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClass implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);
    public void test(){
        System.out.println("TestClass.test");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public  void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("22222222222");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
