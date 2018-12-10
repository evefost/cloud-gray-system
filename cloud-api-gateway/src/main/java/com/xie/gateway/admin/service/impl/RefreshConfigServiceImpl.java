package com.xie.gateway.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.xie.gateway.admin.service.RefreshConfigService;
import com.xie.gateway.api.AppInfo;
import com.xie.gateway.api.UriInfo;
import com.xie.gateway.api.event.AppChangeEvent;
import com.xie.gateway.api.event.GateWayEvent;
import com.xie.gateway.api.event.RefreshEvent;
import com.xie.gateway.api.event.UriChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * 配置信息集群同步服务
 */
@Service
public class RefreshConfigServiceImpl
    implements RefreshConfigService, ApplicationContextAware, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    EurekaDiscoveryClient discoveryClient;
    @Value("${server.context-path:}")
    private String contextPath;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${server.port}")
    private String port;
    private ExecutorService executorService;

    //    @Autowired
    //    RestTemplate restTemplate;
    @Resource
    private ApplicationInfoManager applicationInfoManager;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void refresh(GateWayEvent event) {
        logger.debug("发送刷新网置设置事件 isReplicate:{}", event.getReplicate());
        applicationContext.publishEvent(event);
        if (event.getReplicate() != null && event.getReplicate() == true) {
            return;
        }
        //同步到其它实例
        doSync(event, true);
    }

    private void doSync(GateWayEvent event, boolean isAsync) {

        Map<String, Object> params = new HashMap<>();
        params.put("isReplicate", "true");
        String url = null;
        if (event instanceof RefreshEvent) {
            url = contextPath + "/admin/app/refresh";
        } else if (event instanceof AppChangeEvent) {
            AppChangeEvent appChangeEvent = (AppChangeEvent) event;
            AppInfo data = appChangeEvent.getData();
            params.put("id", data.getId());
            params.put("serviceId", data.getServiceId());
            url = contextPath + "/admin/app/refreshAppInfo";
        } else if (event instanceof UriChangeEvent) {
            UriChangeEvent uriChangeEvent = (UriChangeEvent) event;
            UriInfo data = uriChangeEvent.getData();
            params.put("appId", data.getAppId());
            params.put("serviceId", data.getServiceId());
            params.put("url", data.getUrl());
            url = contextPath + "/admin/app/refreshUriInfo";
        }
        if (url != null) {
            List<ServiceInstance> peerInstances = getPeerInstances();
            if (peerInstances.size() > 0) {
                //                SynConfigTask synConfigTask = new SynConfigTask(restTemplate, peerInstances, url, params);
                //                if(isAsync){
                //                    executorService.submit(synConfigTask);
                //                }else {
                //                    synConfigTask.run();
                //                }
            }
        }
    }

    private List<ServiceInstance> getPeerInstances() {
        List<ServiceInstance> instances = discoveryClient.getInstances(applicationName);
        InstanceInfo localInfo = applicationInfoManager.getInfo();
        List<ServiceInstance> peerInstances = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            if (isSelf(instance, localInfo)) {
                continue;
            }
            peerInstances.add(instance);

        }
        return peerInstances;
    }

    private boolean isSelf(ServiceInstance instance, InstanceInfo localInfo) {
        if (instance instanceof EurekaDiscoveryClient.EurekaServiceInstance) {
            InstanceInfo instanceInfo = ((EurekaDiscoveryClient.EurekaServiceInstance) instance)
                .getInstanceInfo();
            if (instanceInfo.getInstanceId().equals(localInfo.getInstanceId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RefreshEvent event = new RefreshEvent(this);
        applicationContext.publishEvent(event);
        this.executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "sync gateway config exec");
            }
        });
    }

    static class SynConfigTask implements Runnable {

        protected final Logger logger = LoggerFactory.getLogger(getClass());
        RestTemplate restTemplate;
        List<ServiceInstance> peerInstances;
        private String schema = "http://";
        private Map<String, Object> params;
        private String url;

        public SynConfigTask(RestTemplate restTemplate, List<ServiceInstance> peerInstances,
            String url,
            Map<String, Object> params) {
            this.restTemplate = restTemplate;
            this.peerInstances = peerInstances;
            this.url = url;
            this.params = params;
        }

        @Override
        public void run() {
            logger.debug("同步配置信息到其它gateway服务,同步实例数:{}", JSON.toJSONString(peerInstances));
            for (ServiceInstance instance : peerInstances) {
                String requestUrl = schema + instance.getHost() + ":" + instance.getPort() + url;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(params), headers);
                try {
                    restTemplate.postForEntity(requestUrl, entity, Void.class);
                    logger.debug("同步网关设置成功。。");
                } catch (Exception e) {
                    logger.error("同步网关设置失败:{}/{}", requestUrl, e);
                }
            }
        }
    }

}
