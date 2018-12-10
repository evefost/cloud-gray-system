package com.xie.gray.core;

/**
 * @author xie yang
 * @date 2018/10/12-9:37
 */
public enum ServerInstanceStatus {
    /**
     *  正常的
     */
    NORMAL("normal"),
    /**
     * 已经禁用
     */
    //
    DISABLE("disable"),
    /**
     * 灰度
     */
    GRAY("gray");

    private String value;

    ServerInstanceStatus(String value){
        this.value = value;
    }

     public String getValue(){
        return value;
     }

    public static ServerInstanceStatus existValue(String value) {
        for (ServerInstanceStatus strategyType : ServerInstanceStatus.values()) {
            if (strategyType.getValue().equals(value)) {
                return strategyType;
            }
        }
        return null;
    }

}
