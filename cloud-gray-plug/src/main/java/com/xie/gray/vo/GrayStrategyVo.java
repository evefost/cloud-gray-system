package com.xie.gray.vo;

import java.util.List;

/**
 * @author xie yang
 * @date 2018/11/6-20:43
 */
public class GrayStrategyVo {

    /**
     * disable : false serviceId : server_a grayStrategies : [{"disable":false,"type":"ID"}] type : COMPOSITE
     */

    private boolean disable;
    private String serviceId;
    private String type;
    private List<GrayStrategiesBean> grayStrategies;

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GrayStrategiesBean> getGrayStrategies() {
        return grayStrategies;
    }

    public void setGrayStrategies(List<GrayStrategiesBean> grayStrategies) {
        this.grayStrategies = grayStrategies;
    }

    public static class GrayStrategiesBean {

        /**
         * disable : false type : ID
         */

        private boolean disable;
        private String type;

        public boolean isDisable() {
            return disable;
        }

        public void setDisable(boolean disable) {
            this.disable = disable;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
