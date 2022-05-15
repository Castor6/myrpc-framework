package com.castor6.myrpc.framework.core.proxy.javassist;

import com.castor6.myrpc.framework.core.proxy.ProxyFactory;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 17:44
 * @Description
 */
public class JavassistProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class clazz) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                clazz, new JavassistInvocationHandler(clazz));
    }
}