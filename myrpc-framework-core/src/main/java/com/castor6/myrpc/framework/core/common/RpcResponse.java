package com.castor6.myrpc.framework.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 16:22
 * @Description
 */
@Data
@ToString
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 5359096060555795695L;
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据，方法返回结果
     */
    private Object data;
    // 当方法执行过程出现异常，将详细的异常堆栈返回给客户端
    private Throwable throwable;
    //响应数据到达时要放在一个和请求约定好的地方，这样发起请求服务的线程才能正常获取服务的调用结果，那么即是并发哈希表中的uuid对应的value位置
    private String uuid;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public static RpcResponse success(Object data) {
        RpcResponse response = new RpcResponse();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }
    public static RpcResponse fail(ResponseCode code, Throwable e) {
        RpcResponse response = new RpcResponse();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        response.setThrowable(e);
        return response;
    }
}
