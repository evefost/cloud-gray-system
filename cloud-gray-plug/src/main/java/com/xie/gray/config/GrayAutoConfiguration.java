package com.xie.gray.config;

import static com.xie.gray.core.Constant.CONTEXT_PATH;
import static com.xie.gray.core.Constant.SUPPORT_GRAY;

import com.netflix.appinfo.ApplicationInfoManager;
import com.xie.gray.core.GrayLoadBalancerClient;
import com.xie.gray.support.InterceptorBeanProcessor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author xie yang
 * @date 2018/10/10-14:16
 */
@Configuration
public class GrayAutoConfiguration implements ApplicationContextAware, InitializingBean {

    final static Logger logger = LoggerFactory.getLogger(GrayAutoConfiguration.class);


    @Value("${server.context-path:}")
    private String contextPath2;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    private ApplicationContext applicationContext;

    @Autowired
    private SpringClientFactory springClientFactory;


    @Autowired(required = false)
    private ApplicationInfoManager applicationInfoManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Bean
    @Primary
    @ConditionalOnMissingBean(LoadBalancerClient.class)
    public LoadBalancerClient loadBalancerClient() {
        return new GrayLoadBalancerClient(springClientFactory);
    }

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    GrayProperties getGrayProperties(){
        return new GrayProperties();
    }

    @Bean
    @ConditionalOnMissingBean(InterceptorBeanProcessor.class)
    public InterceptorBeanProcessor getFilterBeanProcessor(GrayProperties grayProperties) {
        return new InterceptorBeanProcessor(grayProperties);
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        SpringClientFactory clientFactory = applicationContext.getBean(SpringClientFactory.class);
        Field defaultConfigType = NamedContextFactory.class.getDeclaredField("defaultConfigType");
        defaultConfigType.setAccessible(true);
        defaultConfigType.set(clientFactory, RibbonClientGrayConfig.class);
        //初始化应用metadata
        initGrayMetadata();
    }

    /**
     * 把contextPath保存到metadata，
     * 供网关据contextPath 自动匹配对应的服务并改写路由请求url
     */
    private void initGrayMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put(SUPPORT_GRAY, "true");
        metadata.put(CONTEXT_PATH, contextPath);
        if (StringUtils.isEmpty(contextPath)) {
            if (StringUtils.isEmpty(contextPath2)) {
                metadata.put(CONTEXT_PATH, "/");
            } else {
                metadata.put(CONTEXT_PATH, contextPath2);
            }
        }
        if(applicationInfoManager!= null ){
            applicationInfoManager.registerAppMetadata(metadata);
        }
    }

}
