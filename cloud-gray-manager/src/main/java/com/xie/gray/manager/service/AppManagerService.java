package com.xie.gray.manager.service;


import com.xie.common.vo.Page;
import com.xie.gray.manager.vo.AppListVo;

/**
 * @author xie yang
 * @date 2018/11/8-11:12
 */
public interface AppManagerService {

    AppListVo queryApps();

    AppListVo queryAppsByName(String appID);

    Page<AppListVo.ApplicationsBean.ApplicationBean> queryAppsByPage(String searchWord, int currentPage, int pageSize);
}
