package com.castor6.myrpc.framework.core.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-23 21:49
 * @Description
 */
@Data
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 5359096060555795697L;
    //请求的目标方法，例如findUser
    private String methodName;
    //请求的目标服务名称，例如：com.sise.user.UserService
    private String serviceName;
    //请求参数值
    private Object[] args;
    //请求参数类型（主要是考虑到服务可能存在重载）
    private Class<?>[] paramTypes;
    //响应数据到达时要放在一个和请求约定好的地方，这样发起请求服务的线程才能正常获取服务的调用结果，那么即是并发哈希表中的uuid对应的value位置
    private String uuid;

}
