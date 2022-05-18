package com.castor6.myrpc.framework.core.router;

import com.castor6.myrpc.framework.core.common.ChannelFutureWrapper;
import io.netty.channel.ChannelFuture;

import java.util.List;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 15:31
 * @Description 路由层负责提供向哪个服务提供方发送调用请求的负载均衡策略
 */
public interface MyRouter {
    ChannelFuture select(List<ChannelFutureWrapper> channelFutureWrappers);
}
