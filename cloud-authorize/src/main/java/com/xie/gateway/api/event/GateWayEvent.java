package com.xie.gateway.api.event;

import org.springframework.context.ApplicationEvent;

public class GateWayEvent extends ApplicationEvent {

    private Boolean isReplicate = false;

    public Boolean getReplicate() {
        return isReplicate;
    }

    public void setReplicate(Boolean replicate) {
        isReplicate = replicate;
    }

    public GateWayEvent(Object source) {
        super(source);
    }
}
