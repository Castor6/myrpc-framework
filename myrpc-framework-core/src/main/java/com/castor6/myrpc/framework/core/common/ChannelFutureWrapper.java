package com.castor6.myrpc.framework.core.common;

import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.ToString;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 22:56
 * @Description 封装了连接及其对应的服务提供者的信息
 */
@Data
@ToString
public class ChannelFutureWrapper {
    private ChannelFuture channelFuture;    // netty连接

    private String host;    // 主机名即ip地址

    private Integer port;

    private Integer weight;     // 权重用于负载均衡

    private String group;

    public ChannelFutureWrapper(String host, Integer port,Integer weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }


    public ChannelFutureWrapper() {
    }
}
