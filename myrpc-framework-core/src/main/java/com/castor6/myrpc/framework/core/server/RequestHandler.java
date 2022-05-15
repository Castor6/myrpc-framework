package com.castor6.myrpc.framework.core.server;

import com.castor6.myrpc.framework.core.common.ResponseCode;
import com.castor6.myrpc.framework.core.common.RpcRequest;
import com.castor6.myrpc.framework.core.common.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.castor6.myrpc.framework.core.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 16:44
 * @Description 处理客户端的请求，调用目标服务，将服务的返回结果包装为响应报文
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public RpcResponse handle(RpcRequest rpcRequest) {
        // 向本地注册获取服务（接口）的单例对象
        Object service = PROVIDER_CLASS_MAP.get(rpcRequest.getServiceName());
        if(service == null)     return RpcResponse.fail(ResponseCode.NOT_FOUND_CLASS, new ClassNotFoundException());
        // 通过反射获取目标方法
        Method method = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {     // 服务不存在异常
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD, e);
        }
        // 通过反射执行目标方法
        Object result = null;   // 调用服务的结果
        try {
            if (method.getReturnType().equals(Void.TYPE)) {     // void类型不记录返回值
                method.invoke(service, rpcRequest.getArgs());
            } else {
                result = method.invoke(service, rpcRequest.getArgs());
            }
        } catch (Exception e) {
            // 业务异常
            return RpcResponse.fail(ResponseCode.FAIL, e);
        }
        return RpcResponse.success(result);
    }
}
