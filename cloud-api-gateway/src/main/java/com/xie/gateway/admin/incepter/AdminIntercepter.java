package com.xie.gateway.admin.incepter;//package com.xhg.gateway.admin.incepter;
//
//import com.xhg.gateway.admin.LoginManager;
//import com.xhg.gateway.exception.XhgException;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
//public class AdminIntercepter implements HandlerInterceptor {
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //如果远程服务没有起来，将会跑到这里来
////        if(!LoginManager.isLogin()){
////            throw new XhgException("没权限操作,请登录");
////        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//
//}
