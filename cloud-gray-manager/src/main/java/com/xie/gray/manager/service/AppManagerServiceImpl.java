package com.xie.gray.manager.service;

import static java.util.stream.Collectors.toList;

import com.xie.common.vo.Page;
import com.xie.gray.manager.remote.EurekaFeignClient;
import com.xie.gray.manager.vo.AppListVo;
import com.xie.gray.manager.vo.AppListVo.ApplicationsBean.ApplicationBean;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author xie yang
 * @date 2018/11/8-11:13
 */
@Service
public class AppManagerServiceImpl implements AppManagerService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    private EurekaFeignClient eurekaFeignClient;

    @Override
    public AppListVo queryApps() {
        AppListVo appListVo =eurekaFeignClient.queryApps();
        List<AppListVo.ApplicationsBean.ApplicationBean> application = appListVo.getApplications().getApplication();
        sort(application);
        appListVo.setAppCount(application.size());
        appListVo.setInstanceCount(instanceCount(application));
        return appListVo;
    }

    private int instanceCount(List<ApplicationBean> applications) {
        int count = 0;
        for (ApplicationBean applicationBean : applications) {
            count += applicationBean.getInstance().size();
        }
        return count;
    }

    @Override
    public AppListVo queryAppsByName(String appID) {
        AppListVo appListVo = queryApps();
        if (StringUtils.isEmpty(appID)) {
            return null;
        }
        List<ApplicationBean> application = appListVo.getApplications().getApplication();
        List<ApplicationBean> collect = application.stream().filter(applicationBean -> {
            return Pattern.matches(".*" + appID + ".*", applicationBean.getName());
        }).collect(toList());
        sort(collect);
        appListVo.getApplications().setApplication(collect);
        appListVo.setAppCount(application.size());
        appListVo.setInstanceCount(instanceCount(application));
        return appListVo;
    }

    @Override
    public Page<ApplicationBean> queryAppsByPage(String searchWord, int currentPage, int pageSize) {
        AppListVo appListVo = queryAppsByName(searchWord);
        List<ApplicationBean> application = appListVo.getApplications().getApplication();
        Page<ApplicationBean> page = new Page<>(application, pageSize);
        if (application.isEmpty()) {
            return page;
        }
        page.setCurrentPage(currentPage);
        return page;
    }

    private void sort(List<ApplicationBean> list) {
        list.sort((o1, o2) -> {
            if (isMoreThan(o1.getName(), o2.getName())) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    private static boolean isMoreThan(String pre, String next) {
        char[] c_pre = pre.toCharArray();
        char[] c_next = next.toCharArray();
        int minSize = Math.min(c_pre.length, c_next.length);
        for (int i = 0; i < minSize; i++) {
            if ((int) c_pre[i] > (int) c_next[i]) {
                return true;
            } else if ((int) c_pre[i] < (int) c_next[i]) {
                return false;
            }
        }
        if (c_pre.length > c_next.length) {
            return true;
        }
        return false;
    }


}
