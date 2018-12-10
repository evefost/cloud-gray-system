package com.xie.gateway.admin.controller;

import com.xie.common.vo.ResponseVo;
import com.xie.gateway.admin.bo.GrayIpBo;
import com.xie.gateway.admin.bo.GrayTokenBo;
import com.xie.gateway.admin.service.impl.GrayManagerService;
import com.xie.gray.strategy.GrayStrategy;
import com.xie.gray.strategy.StrategyContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author xie yang
 * @date 2018/11/2-14:55
 */
@RestController
@RequestMapping("/admin/gray")
public class RouteStrategyController {

    @Autowired
    private StrategyContextFactory clientContext;

    @Resource
    EurekaDiscoveryClient discoveryClient;


    @Autowired
    private GrayManagerService grayManagerService;


    @RequestMapping(value = "/add/Ip", method = RequestMethod.POST)
    public ResponseVo addIp(@Valid GrayIpBo params) {
        grayManagerService.addIp(params);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/delete/Ip", method = RequestMethod.POST)
    public ResponseVo deleteIp(@Valid GrayIpBo params) {
        grayManagerService.deleteIp(params);
        return ResponseVo.success();
    }


    @RequestMapping(value = "/add/Token", method = RequestMethod.POST)
    public ResponseVo addToken(@Valid GrayTokenBo params) {
        grayManagerService.addToken(params);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/delete/Token", method = RequestMethod.POST)
    public ResponseVo deleteToken(@Valid GrayTokenBo params) {
        grayManagerService.deleteToken(params);
        return ResponseVo.success();
    }


    /**
     * 获取某服务的策略
     *
     * @param serviceId
     * @return
     */
    @GetMapping(value = "/query/strategies/serviceId")
    public GrayStrategy queryStrategies(String serviceId) {
        GrayStrategy grayStrategy = clientContext.get(serviceId.toUpperCase());
        return grayStrategy;
    }

    /**
     * 获取所有服务的策略
     *
     * @return
     */
    @GetMapping(value = "/query/app/strategies")
    public List<GrayStrategy> queryStrategies2() {
        List<String> serviceIds = discoveryClient.getServices();
        List<GrayStrategy> collect = serviceIds.stream().map(appId -> {
            String serviceId = appId.toUpperCase();
            GrayStrategy grayStrategy = clientContext.get(serviceId);
            return grayStrategy;
        }).collect(toList());
        return collect;
    }

}
