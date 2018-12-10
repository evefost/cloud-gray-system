package com.xie.gray.strategy;

/**
 * @author xie yang
 * @date 2018/11/2-14:59
 */
public enum StrategyType {

    IP("ip"), ID("id"), ZONE_CODE("zone_code"),COMPOSITE("composite"),TOKEN("token");

    private String value;

    StrategyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StrategyType existValue(String value) {
        for (StrategyType strategyType : StrategyType.values()) {
            if (strategyType.getValue().equals(value)) {
                return strategyType;
            }
        }
        return null;
    }
}
