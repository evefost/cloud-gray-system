package com.xie.gateway.support;

import static com.xie.gray.core.Constant.CONTEXT_PATH;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.TimedSupervisorTask;
import com.xie.gateway.config.EnvironmentProperties;
import com.xie.gray.core.GrayRouteContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 */
@Component
public class ServiceHandler implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

    private  ScheduledExecutorService scheduler;

    private  ThreadPoolExecutor serviceMappingExecutor;

    @Resource
    private EurekaDiscoveryClient discoveryClient;

    /**
     * key: contextPath 应对前端转发
     * value: 服务原id,无区分环境
     */
    private volatile Map<String, String> contextPathServiceMap = new HashMap<>();

    /**
     * key: env 对应的环境
     * value: 服务id前辍
     */
    private volatile Map<String,String> envPrefixMap = new HashMap<>();

    private Pattern pattern = Pattern.compile("[0-9]*");


    @Autowired
    private EnvironmentProperties environmentProperties;

    private void start(){
        scheduler = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder()
                        .setNameFormat("RefreshMapping-%d")
                        .setDaemon(true)
                        .build());
        serviceMappingExecutor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactoryBuilder()
                        .setNameFormat("RefreshMapping-%d")
                        .setDaemon(true)
                        .build()
        );
        int expBackOffBound = 10;
        scheduler.schedule(
                new TimedSupervisorTask(
                        "heartbeat",
                        scheduler,
                        serviceMappingExecutor,
                        10,
                        TimeUnit.SECONDS,
                        expBackOffBound,
                        new RenewServiceMappingCache()
                ),
                30, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }


    private   class  RenewServiceMappingCache implements Runnable{
        @Override
        public void run() {
            loadContextPathServiceMapping();
            loadEnvHostMapping();
        }
    }



    /**
     * 通过请求环境参数，把服务映射到服务环境服务的
     * @param env
     * @param serviceId
     * @return
     */
    private String convertServerId2MatcherEnv(String serviceId,String env) {
        if (StringUtils.isEmpty(serviceId)) {
            return "--";
        }
        if (envPrefixMap.isEmpty()) {
            loadEnvHostMapping();
        }
        String envPrefix = envPrefixMap.get(env);
        if (envPrefix == null) {
            return serviceId;
        }
        return envPrefix + "-" + serviceId;
    }

    private  synchronized void loadContextPathServiceMapping() {
        List<String> services = discoveryClient.getServices();
        for (String appId : services) {
            List<ServiceInstance> instances = discoveryClient.getInstances(appId);
            if (instances.isEmpty()) {
                continue;
            }
            ServiceInstance instance = instances.get(0);
            EurekaDiscoveryClient.EurekaServiceInstance eurekaServiceInstance = (EurekaDiscoveryClient.EurekaServiceInstance) instance;
            InstanceInfo instanceInfo = eurekaServiceInstance.getInstanceInfo();
            String contextPath = instanceInfo.getMetadata().get(CONTEXT_PATH);
            if (contextPath == null) {
                continue;
            }
            if ("/".equals(contextPath)) {
                continue;
            }
            String appName = instanceInfo.getAppName();
            String[] split = appName.split("-");
            if (split == null || split.length == 0) {
                contextPathServiceMap.put(contextPath, appName);
                continue;
            }
            String serverIdPreFix = split[0];
            if (isNumeric(serverIdPreFix)) {
                contextPathServiceMap.put(contextPath, appName.substring(serverIdPreFix.length() + 1, appName.length()));
            } else {
                contextPathServiceMap.put(contextPath, appName);
            }
        }
    }

    /**
     * 获取当前请求的contextPath
     * @param context
     * @return
     */
    public String getRequestContextPath(GrayRouteContext context) {
        String servletPath = context.getPath();
        String[] split = servletPath.split("/");
        String incomeContextPath = "/" + split[1];
        return incomeContextPath;
    }

    /**
     * 通过contextPath映射出服务id
     *
     * @param requestContextPath
     * @return
     */
    public String mappingServiceIdByContextPath(String requestContextPath,String env) {
        if (contextPathServiceMap.isEmpty()) {
            loadContextPathServiceMapping();
        }
        String serviceId = contextPathServiceMap.get(requestContextPath);
        if(serviceId == null){
            return null;
        }
        return convertServerId2MatcherEnv(serviceId,env);
    }


    private boolean isNumeric(String str){
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 加载环境映射配置
     */
    private synchronized void loadEnvHostMapping() {
        envPrefixMap = environmentProperties.getHostMap();
//        ResponseBean<List<EnvHostMapVo>> listResponseBean = omsFeignService.queryEnvList();
//        List<EnvHostMapVo> data = listResponseBean.getResponseBody().getData();
//        for (EnvHostMapVo mapVo : data) {
//            int i = mapVo.getEnvHost().indexOf(".");
//            String substring = mapVo.getEnvHost().substring(0, i);
//            envPrefixMap.put(substring, mapVo.getAppPrefix());
//        }
    }
}
