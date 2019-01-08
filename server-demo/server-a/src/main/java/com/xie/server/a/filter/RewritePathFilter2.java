package com.xie.server.a.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 改写请求path，配置到不同环境服务
 * @author xie
 */
//@Component
//@Order(2)
public class RewritePathFilter2 extends TestClass {

    private static final Logger log = LoggerFactory.getLogger(RewritePathFilter2.class);

    private int number ;


   public RewritePathFilter2(int number){
        this.number = number;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }




    @Override
    public void destroy() {

    }
}