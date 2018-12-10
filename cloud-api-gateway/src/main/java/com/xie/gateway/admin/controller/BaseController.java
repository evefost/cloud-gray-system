package com.xie.gateway.admin.controller;

//import javax.servlet.http.HttpServletRequest;

public class BaseController {

    //    @Resource
    //    protected HttpServletRequest request;

    protected int getCurrentPage() {
        //        String currentPage = request.getParameter("currentPage");
        //        if(StringUtils.isEmpty(currentPage)){
        //            return  1;
        //        }
        //        return Integer.parseInt(currentPage);
        return 1;
    }

    protected int getPageSize() {
        //        String pageSize = request.getParameter("pageSize");
        //        if(StringUtils.isEmpty(pageSize)){
        //            return  10;
        //        }
        //        return Integer.parseInt(pageSize);
        return 10;
    }
}
