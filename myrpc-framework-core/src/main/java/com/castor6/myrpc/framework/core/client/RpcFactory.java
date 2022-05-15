package com.castor6.myrpc.framework.core.client;


import com.castor6.myrpc.framework.core.proxy.ProxyFactory;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 17:21
 * @Description 动态生成代理对象的工厂类
 */
public class RpcFactory {

    public ProxyFactory proxyFactory;

    public RpcFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> tClass) throws Throwable {
        return proxyFactory.getProxy(tClass);
    }
}