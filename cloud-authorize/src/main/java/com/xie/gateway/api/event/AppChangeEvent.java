package com.xie.gateway.api.event;

import com.xie.gateway.api.AppInfo;

public class AppChangeEvent extends GateWayEvent {

    private AppInfo data;

    public AppChangeEvent(Object source, AppInfo data) {
        super(source);
        this.data = data;
    }

    public AppInfo getData() {
        return data;
    }

    public void setData(AppInfo data) {
        this.data = data;
    }
}
