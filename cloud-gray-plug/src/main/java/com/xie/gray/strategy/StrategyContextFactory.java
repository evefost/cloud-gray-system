package com.xie.gray.strategy;


import com.xie.gray.core.GrayRouteContext;
import com.xie.gray.strategy.impl.CompositeGrayStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * @author xie yang
 * @date 2018/11/1-17:36
 */
public class StrategyContextFactory extends NamedContextFactory {

    private Map<String, GrayStrategy> strategyMap = new HashMap<>();


    public StrategyContextFactory(Class<?> defaultConfigType) {
        super(defaultConfigType, "strategy", "gray.client.name");
    }

    public GrayStrategy get(GrayRouteContext context) {
       return get(context.getServiceId());
    }

    public GrayStrategy get(String clientName) {
        GrayStrategy strategy = strategyMap.get(clientName);
        if (strategy != null) {
            return strategy;
        }
        CompositeGrayStrategy compositeGrayStrategy = null;
        List<GrayStrategy> strategyList = new ArrayList<>();
        Map<String, GrayStrategy> instances = getInstances(clientName, GrayStrategy.class);
        Set<Map.Entry<String, GrayStrategy>> entries = instances.entrySet();
        for (Map.Entry<String, GrayStrategy> entry : entries) {
            GrayStrategy value = entry.getValue();
            value.setServiceId(clientName);
            if (value instanceof CompositeGrayStrategy) {
                compositeGrayStrategy = (CompositeGrayStrategy) value;
            } else {
                strategyList.add(value);
            }
        }
        for (GrayStrategy grayStrategy : strategyList) {
            compositeGrayStrategy.add(grayStrategy);
        }
        strategyMap.put(clientName,compositeGrayStrategy);
        return compositeGrayStrategy;
    }

}
