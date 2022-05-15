package com.castor6.myrpc.framework.core.proxy;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 17:30
 * @Description
 */
public interface ProxyFactory {

    <T> T getProxy(final Class clazz) throws Throwable;
}
