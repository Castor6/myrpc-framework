package com.castor6.myrpc.framework.core.proxy.jdk;

import com.castor6.myrpc.framework.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 17:29
 * @Description
 */
public class JDKProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(final Class clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new JDKClientInvocationHandler(clazz));
    }

}
