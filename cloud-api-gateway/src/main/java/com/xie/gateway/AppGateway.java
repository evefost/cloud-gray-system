package com.xie.gateway;

import com.xie.common.exception.XException;
import com.xie.gateway.admin.vo.AppVo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class AppGateway {

    public static void main(String[] args) {
        SpringApplication.run(AppGateway.class, args);
    }

    //普通一个rest接口
    @RequestMapping("/hystrixfallback")
    public String hystrixfallback() {
        if (true) {
            throw new RuntimeException("假装抛异常");
        }
        return "This is a fallback";
    }

    @GetMapping("/cloudgateway/admin/test")
    public String hystrixfallback2() {
        //        if(true){
        //            throw new XhgException("业务异常");
        //        }
        return "This is a fallback2";
    }

    @GetMapping("/cloudgateway/admin/test2")
    public String hystrixfallback2(@RequestParam Integer userName) {
        if (true) {
            throw new XException("业务异常");
        }
        return "This is a fallback2";
    }

    @PostMapping("/hystrixfallback4")
    public String hystrixfallback4(@RequestBody AppVo appVo) {
        if (true) {
            throw new XException("业务异常");
        }
        return "This is a fallback2";
    }

    //这里定义一堆路由信息，跟上面的接口类似
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        //@formatter:off
//        return builder.routes()
//            .route("path_route", r -> r.path("/get")
//                .uri("http://httpbin.org"))
//            .route("host_route", r -> r.host("*.myhost.org")
//                .uri("http://httpbin.org"))
//            .route("rewrite_route", r -> r.host("*.rewrite.org")
//                .filters(f -> f.rewritePath("/foo/(?<segment>.*)",
//                    "/${segment}"))
//                .uri("http://httpbin.org"))
//            .route("hystrix_route", r -> r.host("*.hystrix.org")
//                .filters(f -> f.hystrix(c -> c.setName("slowcmd")))
//                .uri("http://httpbin.org"))
//            .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
//                .filters(f -> f
//                    .hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
//                .uri("http://httpbin.org"))
//            .route("limit_route", r -> r
//                .host("*.limited.org").and().path("/anything/**")
//                .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
//                .uri("http://httpbin.org"))
//            .route("websocket_route", r -> r.path("/echo")
//                .uri("ws://localhost:9000"))
//            .build();
//        //@formatter:on
//    }

    @Bean
    RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 2);
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.httpBasic().and().csrf().disable().authorizeExchange()
            .pathMatchers("/anything/**")
            .authenticated()
            .anyExchange().permitAll().and().build();
    }

    @Bean
    public MapReactiveUserDetailsService reactiveUserDetailsService() {
        UserDetails user =
            User.withDefaultPasswordEncoder().username("user").password("password").roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

}
