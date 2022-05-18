package com.castor6.myrpc.framework.core.common.event.destory;

import com.castor6.myrpc.framework.core.common.event.MyRpcEvent;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 16:46
 * @Description 服务提供方的注销服务事件
 */
public class MyRpcDestroyEvent implements MyRpcEvent {

    private Object data;

    public MyRpcDestroyEvent(Object data) {
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
