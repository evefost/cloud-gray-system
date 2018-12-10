package com.xie.gray.manager.vo;



/**
 * @author xie yang
 * @date 2018/11/6-14:04
 */
public class AppInfoVo {

    /**
     * application : {"name":"XHG-EUREKA-SERVER","instance":[{"instanceId":"10.10.10.43:xhg-eureka-server:8761","hostName":"10.10.10.43","app":"XHG-EUREKA-SERVER","ipAddr":"10.10.10.43","status":"UP","overriddenstatus":"UNKNOWN","port":{"$":8761,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":30,"durationInSecs":90,"registrationTimestamp":1541024477281,"lastRenewalTimestamp":1541527144506,"evictionTimestamp":0,"serviceUpTimestamp":1541024477281},"metadata":{"management.port":"8761"},"homePageUrl":"http://10.10.10.43:8761/","statusPageUrl":"http://10.10.10.43:8761/info","healthCheckUrl":"http://10.10.10.43:8761/health","vipAddress":"xhg-eureka-server","secureVipAddress":"xhg-eureka-server","isCoordinatingDiscoveryServer":"false","lastUpdatedTimestamp":"1541024477281","lastDirtyTimestamp":"1540981184412","actionType":"ADDED"},{"instanceId":"localhost:xhg-eureka-server:8761","hostName":"10.10.10.210","app":"XHG-EUREKA-SERVER","ipAddr":"10.10.10.210","status":"UP","overriddenstatus":"UNKNOWN","port":{"$":8761,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":30,"durationInSecs":90,"registrationTimestamp":1538238319477,"lastRenewalTimestamp":1541527158974,"evictionTimestamp":0,"serviceUpTimestamp":1538238319246},"metadata":{"management.port":"8761"},"homePageUrl":"http://10.10.10.210:8761/","statusPageUrl":"http://10.10.10.210:8761/info","healthCheckUrl":"http://10.10.10.210:8761/health","vipAddress":"xhg-eureka-server","secureVipAddress":"xhg-eureka-server","isCoordinatingDiscoveryServer":"true","lastUpdatedTimestamp":"1538238319477","lastDirtyTimestamp":"1538238317562","actionType":"ADDED"},{"instanceId":"10.10.10.211:xhg-eureka-server:8761","hostName":"10.10.10.211","app":"XHG-EUREKA-SERVER","ipAddr":"10.10.10.211","status":"UP","overriddenstatus":"UNKNOWN","port":{"$":8761,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":30,"durationInSecs":90,"registrationTimestamp":1538238318043,"lastRenewalTimestamp":1541527159799,"evictionTimestamp":0,"serviceUpTimestamp":1538238318043},"metadata":{"management.port":"8761"},"homePageUrl":"http://10.10.10.211:8761/","statusPageUrl":"http://10.10.10.211:8761/info","healthCheckUrl":"http://10.10.10.211:8761/health","vipAddress":"xhg-eureka-server","secureVipAddress":"xhg-eureka-server","isCoordinatingDiscoveryServer":"false","lastUpdatedTimestamp":"1538238318043","lastDirtyTimestamp":"1533568646620","actionType":"ADDED"},{"instanceId":"10.10.10.212:xhg-eureka-server:8761","hostName":"10.10.10.212","app":"XHG-EUREKA-SERVER","ipAddr":"10.10.10.212","status":"UP","overriddenstatus":"UNKNOWN","port":{"$":8761,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":30,"durationInSecs":90,"registrationTimestamp":1540911244595,"lastRenewalTimestamp":1541527140066,"evictionTimestamp":0,"serviceUpTimestamp":1538238318044},"metadata":{"management.port":"8761"},"homePageUrl":"http://10.10.10.212:8761/","statusPageUrl":"http://10.10.10.212:8761/info","healthCheckUrl":"http://10.10.10.212:8761/health","vipAddress":"xhg-eureka-server","secureVipAddress":"xhg-eureka-server","isCoordinatingDiscoveryServer":"false","lastUpdatedTimestamp":"1540911244596","lastDirtyTimestamp":"1540867949414","actionType":"ADDED"}]}
     */

    private AppListVo.ApplicationsBean.ApplicationBean application;

    public AppListVo.ApplicationsBean.ApplicationBean getApplication() {
        return application;
    }

    public void setApplication(AppListVo.ApplicationsBean.ApplicationBean application) {
        this.application = application;
    }
}
