package com.castor6.myrpc.framework.core.common.event.nodedatachange;

import com.castor6.myrpc.framework.core.common.event.MyRpcEvent;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 16:12
 * @Description
 */
public class MyRpcNodeDataChangeEvent implements MyRpcEvent {

    private Object data;

    public MyRpcNodeDataChangeEvent(Object data) {
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
