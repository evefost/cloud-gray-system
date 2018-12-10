package com.xie.gray.manager.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;

/**
 * @Name:
 * @Description:
 * @Copyright: Copyright (c) 2018
 * @Author haojing
 * @Create Date 2018/9/29
 * @Version 1.0.0
 */
public abstract class BaseController {

    @Autowired
    HttpServletRequest request;




    protected int getPageSize() {
        String pageSize = request.getParameter("pageSize");
        int size = 10;
        if (!StringUtils.isEmpty(pageSize)) {
            size = Integer.valueOf(pageSize);
        }
        return size;
    }

    protected int getCurrentPage() {
        String currentPage = request.getParameter("currentPage");
        int current = 1;
        if (!StringUtils.isEmpty(currentPage)) {
            current = Integer.valueOf(currentPage);
        }
        return current;
    }



}
