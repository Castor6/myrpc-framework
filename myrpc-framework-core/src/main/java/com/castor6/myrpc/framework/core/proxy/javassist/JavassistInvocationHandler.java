package com.castor6.myrpc.framework.core.proxy.javassist;

import com.castor6.myrpc.framework.core.common.RpcRequest;
import com.castor6.myrpc.framework.core.common.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;
import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 17:42
 * @Description
 */
public class JavassistInvocationHandler implements InvocationHandler {


    private final static Object OBJECT = new Object();

    private Class<?> clazz;

    public JavassistInvocationHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setArgs(args);
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setServiceName(clazz.getName());
        rpcRequest.setUuid(UUID.randomUUID().toString());
        rpcRequest.setParamTypes(method.getParameterTypes());   // 记得设置方法参数！！！
        RESP_MAP.put(rpcRequest.getUuid(), OBJECT);
        SEND_QUEUE.add(rpcRequest);
        long beginTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - beginTime < 3*1000) {
            Object object = RESP_MAP.get(rpcRequest.getUuid());
            if (object instanceof RpcResponse) {
                return ((RpcResponse)object).getData();
            }
        }
        throw new TimeoutException("client wait server's response timeout!");
    }
}
