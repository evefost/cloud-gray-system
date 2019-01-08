package com.xie.server.a.config;

import com.xie.server.a.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Set-Cookie")
            .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor())
            .excludePathPatterns("/swagger-resources/**",
            "/webjars/**", "/v2/**",
            "/swagger-ui.html/**").addPathPatterns("/**");
    }

    @Bean
    LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    @Bean
//    @Order(2)
//    RewritePathFilter2 getRewritePathFilter2(){
//        return new RewritePathFilter2(2);
//    }
}
