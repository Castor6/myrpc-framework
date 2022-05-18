package com.castor6.myrpc.framework.core.common.event;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-17 16:32
 * @Description 事件监听器用于捕获出现的事件并处理（这块做成类似kafka的消息队列了，不过是消费者专门处理一个类型的事件）
 */
public interface MyRpcListener<T> {

    void callBack(Object t);    // 处理事件

}
