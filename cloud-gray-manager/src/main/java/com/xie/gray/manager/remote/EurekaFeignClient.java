package com.xie.gray.manager.remote;


import com.xie.gray.manager.vo.AppInfoVo;
import com.xie.gray.manager.vo.AppInstanceVo;
import com.xie.gray.manager.vo.AppListVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xie yang
 * @date 2018/10/11-10:30
 */
@FeignClient(name = "eureka-server")
public interface EurekaFeignClient {

    /**
     * 设置服务状态
     */
    @PutMapping(value = "/eureka/apps/{appID}/{instanceID}/metadata")
    String updateServerStatus(@PathVariable("appID") String appID, @PathVariable("instanceID") String instanceID,
                              @RequestParam("instance_status") String instance_status);


    @GetMapping(value = "/eureka/apps")
    AppListVo queryApps();

    @GetMapping(value = "/eureka/apps/{appID}")
    AppInfoVo queryAppInstancesByAppId(@PathVariable("appID") String appID);


    @GetMapping(value = "/eureka/apps/{appID}/{instanceID}")
    AppInstanceVo queryInstanceByInstanceId(@PathVariable("appID") String appID,
                                            @PathVariable("instanceID") String instanceID);
}
