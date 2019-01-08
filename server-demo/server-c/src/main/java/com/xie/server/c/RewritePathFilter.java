package com.xie.server.c;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 改写请求path，配置到不同环境服务
 * @author xie
 */
@Component
@Order(1)
public class RewritePathFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RewritePathFilter.class);



//    @Autowired
//    private ServiceHandler serviceHandler;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        if(true){
            throw new RuntimeException("未登录。。。。");
        }
        System.out.println("11111111111");
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}