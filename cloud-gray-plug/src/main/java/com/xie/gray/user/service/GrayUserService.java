package com.xie.gray.user.service;


import com.xie.common.vo.Page;
import com.xie.gray.user.vo.GrayUser;
import com.xie.gray.user.vo.UserParams;

public interface GrayUserService {

    /**
     * 获取用户列表
     * @param params
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<GrayUser> queryUsersByPage(UserParams params, int currentPage, int pageSize);
}
