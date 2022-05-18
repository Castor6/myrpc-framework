package com.castor6.myrpc.framework.core.router;

import com.castor6.myrpc.framework.core.common.ChannelFutureWrapper;
import io.netty.channel.ChannelFuture;

import java.nio.channels.Selector;
import java.util.List;
import java.util.Random;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 17:24
 * @Description 随机路由
 */
public class MyRandomRouter implements MyRouter{
    @Override
    public ChannelFuture select(List<ChannelFutureWrapper> channelFutureWrappers) {
        return channelFutureWrappers.get(new Random().nextInt(channelFutureWrappers.size())).getChannelFuture();
    }
}
