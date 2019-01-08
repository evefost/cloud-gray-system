package com.xie.gray.test;

import static com.xie.gray.core.Constant.CONTEXT_PATH;

import com.alibaba.fastjson.JSON;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.xie.gray.core.Constant;
import com.xie.gray.core.GrayUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 路由链路测试
 *
 * @author xie
 */
@RestController
@RequestMapping("/gray/test")
public class GrayRouteTestController {

    private Logger logger = LoggerFactory.getLogger(GrayRouteTestController.class);

    @Resource
    private EurekaDiscoveryClient discoveryClient;

    @Resource()
    private ApplicationInfoManager applicationInfoManager;

    @Resource
    private RestTemplate restTemplate;

    /**
     * @param nodes  需要请求的顺序服务信息
     * @return
     */
    @RequestMapping(value = "linksTest", method = RequestMethod.POST)
    public TestResult nodeTest(@RequestBody List<String> nodes) {
        InstanceInfo startNodeServer = applicationInfoManager.getInfo();
        String currentServiceId = startNodeServer.getAppName().toLowerCase();
        if (!nodes.get(0).equals(currentServiceId)) {
            nodes.add(0,currentServiceId);
        }
        //处理链路节点信息
        RequestNode requestNode = convert2RequestNode(nodes);
        List<ResultNode> resultNodes = internalNodeTest(requestNode);
        ResultNode startNode = new ResultNode();
        startNode.setServiceId(startNodeServer.getAppName().toLowerCase());
        startNode.setHost(startNodeServer.getHostName());
        startNode.setPort(startNodeServer.getPort());
        String serverStatus = startNodeServer.getMetadata().get(Constant.INSTANCE_STATUS);
        startNode.setInstanceStatus(serverStatus);
        resultNodes.add(startNode);
        Comparator<ResultNode> tComparator = Collections.reverseOrder();
        resultNodes.sort(tComparator);
        TestResult testResult = new TestResult();
        ResultNode resultNode = resultNodes.get(resultNodes.size() - 1);
        testResult.setSuccess(resultNode.isSuccess());
        testResult.setMessage(resultNode.getMessage());
        testResult.setNodeList(resultNodes);
        testResult.setTotal(nodes.size());
        testResult.setRoute2Gray(GrayUtils.isGray());
        testResult.setFailureCount(resultNode.isSuccess() ? 0 : nodes.size() - (resultNodes.size() - 1));
        return testResult;

    }


    @RequestMapping(value = "internalNodeTest", method = RequestMethod.POST)
    public List<ResultNode> internalNodeTest(@RequestBody RequestNode requestNode) {
        //获取下一请求服务节点信息
        RequestNode next = requestNode.getNext();
        if (next == null) {
            logger.info("request is end");
            List<ResultNode> list = new ArrayList<>();
            return list;
        }

        GrayUtils.currentSelectServer.set(new Server(next.getServiceId()));
        try {
            ResultNode nextNodeServer = new ResultNode();
            nextNodeServer.setIndex(next.getIndex());
            nextNodeServer.setServiceId(next.getServiceId());
            List<ResultNode> resultNodes = new ArrayList<>();
            //处理服务请求url
            String serverUrl = getServerUrl(next.getServiceId());
            if (StringUtils.isEmpty(serverUrl)) {
                nextNodeServer.setMessage("无" + next.getServiceId() + "服务信息");
                nextNodeServer.setSuccess(false);
                resultNodes.add(nextNodeServer);
                return resultNodes;
            }
            HttpHeaders requestHeaders = new HttpHeaders();
            //传递灰度标签
            requestHeaders.add(Constant.ROUTE_TO_GRAY, String.valueOf(GrayUtils.isGray()));
            HttpEntity<RequestNode> requestEntity = new HttpEntity<>(next, requestHeaders);

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
                resultNodes = JSON.parseArray(response.getBody(), ResultNode.class);
                Server testServer = GrayUtils.getTestServer();
                nextNodeServer.setHost(testServer.getHost());
                nextNodeServer.setPort(testServer.getPort());
                if (testServer instanceof DiscoveryEnabledServer) {
                    DiscoveryEnabledServer enabledServer = (DiscoveryEnabledServer) testServer;
                    InstanceInfo instanceInfo = enabledServer.getInstanceInfo();
                    String serverStatus = instanceInfo.getMetadata().get(Constant.INSTANCE_STATUS);
                    nextNodeServer.setInstanceStatus(serverStatus);
                    nextNodeServer.setIndex(next.getIndex());
                }
            } catch (Exception ex) {
                //下一服务节点请求失败
                logger.warn("links test failure:{}", ex.getMessage());
                nextNodeServer.setMessage("[route_to_gray:" + GrayUtils.isGray() + "]" + ex.getMessage() + serverUrl);
                nextNodeServer.setSuccess(false);
                resultNodes.add(nextNodeServer);
            }

            resultNodes.add(nextNodeServer);
            return resultNodes;
        } finally {
            GrayUtils.removeTestServer();
        }
    }


    private RequestNode convert2RequestNode(List<String> nodes) {
        RequestNode nextNode = null;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (nextNode == null) {
                nextNode = new RequestNode(nodes.get(i), i);
            } else {
                RequestNode tNextNode = new RequestNode(nodes.get(i), i);
                tNextNode.setNext(nextNode);
                nextNode = tNextNode;
            }
        }
        return nextNode;
    }

    /**
     * 处理请求url
     * @param nextNodeId
     * @return
     */
    private String getServerUrl(String nextNodeId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(nextNodeId);
        if (instances.isEmpty()) {
            return null;
        }
        ServiceInstance serviceInstance = instances.get(0);
        String contextPath = serviceInstance.getMetadata().get(CONTEXT_PATH);
        String temContextPath = contextPath == null ? "/" : contextPath;
        String url = "http://" + nextNodeId.toLowerCase() + temContextPath + "/gray/test/internalNodeTest";
        return url;
    }
}
