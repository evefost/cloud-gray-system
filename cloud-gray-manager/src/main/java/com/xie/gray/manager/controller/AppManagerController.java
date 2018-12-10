package com.xie.gray.manager.controller;


import com.xie.common.vo.Page;
import com.xie.common.vo.ResponseVo;
import com.xie.gray.core.ServerInstanceStatus;
import com.xie.gray.manager.bo.Instance;
import com.xie.gray.manager.remote.EurekaFeignClient;
import com.xie.gray.manager.service.AppManagerService;
import com.xie.gray.manager.vo.AppInstanceVo;
import com.xie.gray.manager.vo.AppListVo;
import com.xie.gray.manager.vo.AppListVo.ApplicationsBean.ApplicationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 应用管理
 *
 * @author xie yang
 * @date 2018/11/1-11:26
 */
@RestController
@RequestMapping("/app")
public class AppManagerController extends BaseController {

    @Autowired
    private EurekaFeignClient eurekaFeiginClient;

    @Autowired
    private AppManagerService managerService;


    /**
     * 更新服务状态
     */
    @PostMapping(value = "/update/instance/status")
    public ResponseVo updateServerStatus(@RequestBody @Valid Instance params) {
        ServerInstanceStatus serverStatus = ServerInstanceStatus.existValue(params.getInstanceStatus());
        if (serverStatus == null) {
            return ResponseVo.failure("状态值不合法");
        }
        eurekaFeiginClient.updateServerStatus(params.getAppID(),params.getInstanceID(),serverStatus.getValue());
        return ResponseVo.success();
    }


    /**
     * 获取所有应用信息
     */
    @GetMapping(value = "/query/apps")
    public ResponseVo<AppListVo> queryApps() {
        return ResponseVo.success(managerService.queryApps());
    }


    /**
     * 分页获取应用信息
     */
    @GetMapping(value = "/query/apps/byPage")
    public ResponseVo<Page<ApplicationBean>> queryAppsByPage(String appID) {
        return ResponseVo.success(managerService.queryAppsByPage(appID, getCurrentPage(), getPageSize()));
    }





    @GetMapping(value = "/query/apps/byName")
    public ResponseVo<AppListVo> queryAppsByName(String appID) {
        return ResponseVo.success(managerService.queryAppsByName(appID));
    }


    /**
     * eureka-server 获取某应所的某个实例详情
     */
    @GetMapping(value = "/query/apps/instance")
    public ResponseVo<AppInstanceVo> queryAppInstanceById(String appID, String instanceID) {
        AppInstanceVo applicationBean = eurekaFeiginClient.queryInstanceByInstanceId(appID, instanceID);
        return ResponseVo.success(applicationBean);
    }

}
