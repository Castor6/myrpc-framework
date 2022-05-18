package com.castor6.myrpc.framework.core.common.event.update;

import com.castor6.myrpc.framework.core.common.event.MyRpcEvent;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-17 16:30
 * @Description 节点更新事件（之所以不直接将数据拆开放入事件类中，是为了通用事件，分别定义数据类）
 */
public class MyRpcUpdateEvent implements MyRpcEvent {

    private Object data;

    public MyRpcUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public MyRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
