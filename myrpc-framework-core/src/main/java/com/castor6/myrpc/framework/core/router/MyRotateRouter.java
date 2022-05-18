package com.castor6.myrpc.framework.core.router;

import com.castor6.myrpc.framework.core.common.ChannelFutureWrapper;
import io.netty.channel.ChannelFuture;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 17:27
 * @Description
 */
public class MyRotateRouter implements MyRouter{
    private AtomicLong referenceTimes = new AtomicLong(0);  // 需要一个原子类，保证线程安全

    @Override
    public ChannelFuture select(List<ChannelFutureWrapper> channelFutureWrappers) {
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % channelFutureWrappers.size());
        return channelFutureWrappers.get(index).getChannelFuture();
    }
}
