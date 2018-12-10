mvc 异常拦截
1.FilterChain
ApplicationFilterChain 
Filter 可能抛出异常
RequestContextFilter
2 DispatcherServlet 处理异常
processDispatchResult
3.HandlerInterceptor

Controller
BasicErrorController


org.apache.catalina.core.StandardWrapperValve
ApplicationFilterChain.doFilter
  private void exception(Request request, Response response,
                           Throwable exception) {
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setError();
    }
    
    
StandardWrapperValve extend ValveBase
 标准的封装处理
 invoke
StandardContextValve extends ValveBase
  标准的上下文
 invoke
 
 
    public final void i
StandardHostValve   
    status(request, response);
    custom
    
    ApplicationDispatcher
    
    forward
    invoke