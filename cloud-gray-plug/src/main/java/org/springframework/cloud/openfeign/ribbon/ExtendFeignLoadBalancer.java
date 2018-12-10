
package org.springframework.cloud.openfeign.ribbon;

import com.netflix.client.ClientException;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;

import java.io.IOException;
import java.net.URI;

public class ExtendFeignLoadBalancer extends FeignLoadBalancer {

    final static Logger logger = LoggerFactory.getLogger(ExtendFeignLoadBalancer.class);

    protected int connectTimeout;

    protected int readTimeout;

    private String eurekaUrl;

    public void setEurekaUrl(String eurekaUrl) {
        this.eurekaUrl = eurekaUrl;
    }

    public ExtendFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig,
                                   ServerIntrospector serverIntrospector) {
        super(lb, clientConfig, serverIntrospector);
    }

    @Override
    public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride)
        throws IOException {
        Request.Options options;
        if (configOverride != null) {
            options = new Request.Options(
                configOverride.get(CommonClientConfigKey.ConnectTimeout,
                    this.connectTimeout),
                (configOverride.get(CommonClientConfigKey.ReadTimeout,
                    this.readTimeout)));
        } else {
            options = new Request.Options(this.connectTimeout, this.readTimeout);
        }
        Response response = null;
        try {
            response = request.client().execute(request.toRequest(), options);
        } catch (Exception ex) {
            logger.error("远程调用失败:{}/当前注册中心地址:{}", request.getUri(), eurekaUrl, ex);
            throw ex;
        }
        return new RibbonResponse(request.getUri(), response);
    }


    @Override
    public Server getServerFromLoadBalancer(URI original, Object loadBalancerKey)
        throws ClientException {
        try {
            return super.getServerFromLoadBalancer(original, loadBalancerKey);
        } catch (ClientException ex) {
            logger.error("远程服务信息无效:{}/当前注册中心地址:{}", original, eurekaUrl, ex);
            throw new RuntimeException("获取远程服务信息失败");
        }
    }

}
