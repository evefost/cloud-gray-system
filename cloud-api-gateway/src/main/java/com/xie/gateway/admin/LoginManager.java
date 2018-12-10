package com.xie.gateway.admin;//package com.xhg.gateway.admin;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class LoginManager {
//
//    @Value("${admin.login.expire:5}")
//    private int expire;
//
//    public static  long expireTime = 1000 * 60*5;
//
//    private final static   ConcurrentHashMap<String/*token*/, Admin> admins = new ConcurrentHashMap<>();
//
//    Timer timer = new Timer();
//
//    public  String login() {
//        String token = UUID.randomUUID().toString();
//        long expire = System.currentTimeMillis() + expireTime;
//        Admin admin = new Admin();
//        admin.setToken(token);
//        admin.setExpire(expire);
//        admins.put(token, admin);
//       return token;
//    }
//
//    public static boolean logout(){
////        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
////        if (requestAttributes instanceof ServletRequestAttributes) {
////            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
////            String token = attributes.getRequest().getHeader("token");
////            if(token != null){
////                return admins.remove(token) != null;
////            }
////            return false;
////        } else {
////            return false;
////        }
//        return true;
//    }
//
//    public static boolean isLogin() {
//        RequestAttributes requestAttributes =  RequestContextHolder.currentRequestAttributes();
//        if (requestAttributes instanceof ServletRequestAttributes) {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
//            String token = attributes.getRequest().getHeader("token");
//            if(token == null){
//                return false;
//            }
//            Admin admin = admins.get(token);
//            if(admin ==null){
//                return false;
//            }
//            if(admin.getExpire()<System.currentTimeMillis()){
//                admins.remove(token);
//                return false;
//            }
//            long expire = System.currentTimeMillis() + expireTime;
//            admin.setExpire(expire);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @PostConstruct
//    private void init(){
//        expireTime =1000*60*expire;
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            public void run() {
//                long currentTime = System.currentTimeMillis();
//                admins.forEach((token,admin)->{
//                    if(admin.expire<currentTime){
//                        admins.remove(token);
//                    }
//                });
//            }
//        }, 2000 , 1000);
//    }
//
//    @PreDestroy
//    private void destroy(){
//        timer.cancel();
//    }
//
//    static class Admin {
//        private String token;
//        private long expire;
//
//        public String getToken() {
//            return token;
//        }
//
//        public void setToken(String token) {
//            this.token = token;
//        }
//
//        public long getExpire() {
//            return expire;
//        }
//
//        public void setExpire(long expire) {
//            this.expire = expire;
//        }
//    }
//}
