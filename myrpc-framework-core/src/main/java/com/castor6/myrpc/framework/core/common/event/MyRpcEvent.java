package com.castor6.myrpc.framework.core.common.event;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-17 16:29
 * @Description 事件接口
 */
public interface MyRpcEvent {
    Object getData();
    MyRpcEvent setData(Object data);
}
