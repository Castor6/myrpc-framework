package com.castor6.myrpc.framework.core.common.event.update;

import com.castor6.myrpc.framework.core.client.ConnectionHandler;
import com.castor6.myrpc.framework.core.common.ChannelFutureWrapper;
import com.castor6.myrpc.framework.core.common.event.MyRpcListener;
import com.castor6.myrpc.framework.core.common.util.CommonUtils;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-17 17:59
 * @Description 处理服务提供方上下线的事件
 */
public class ServiceUpdateListener implements MyRpcListener<MyRpcUpdateEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUpdateListener.class);

    /**
     * @Author Castor6
     * @Description 更新服务消费者本地缓存的服务提供者连接
     *              1. 复用还在线上的服务提供者的连接
     *              2. 创建并加入新上线的服务提供者的连接
     * @Date 2022/5/18 11:30
     * @Param t：更新本地缓存事件的数据
     * @return
     */
    @Override
    public void callBack(Object t) {
        URLChangeWrapper urlChangeWrapper = (URLChangeWrapper) t;
        // 获取已连接的服务的服务提供者列表
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(urlChangeWrapper.getServiceName());
        if (CommonUtils.isEmptyList(channelFutureWrappers)) {
            LOGGER.error("[ServiceUpdateListener] channelFutureWrappers is empty");
            return;
        } else {
            // 获取当前服务的服务提供者所在路径列表
            List<String> matchProviderUrl = urlChangeWrapper.getProviderUrl();
            // 构筑新的服务提供者列表（复用还在上线的旧连接）
            Set<String> finalUrl = new HashSet<>();     // 用于装填还能复用的服务提供者地址，以及在加上新服务提供者地址时提供判断
            List<ChannelFutureWrapper> finalChannelFutureWrappers = new ArrayList<>();
            for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
                String oldServerAddress = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
                //如果老的url没有，说明该服务提供者已经下线了，所以不要把旧连接放入新列表中
                if (!matchProviderUrl.contains(oldServerAddress)) {
                    continue;
                } else {
                    finalChannelFutureWrappers.add(channelFutureWrapper);
                    finalUrl.add(oldServerAddress);
                }
            }
            //此时老的url已经被移除了，开始检查是否有新的url
            List<ChannelFutureWrapper> newChannelFutureWrapper = new ArrayList<>();
            for (String newProviderUrl : matchProviderUrl) {
                if (!finalUrl.contains(newProviderUrl)) {   // 新上线的服务提供者
                    ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
                    String host = newProviderUrl.split(":")[0];     // ip地址
                    Integer port = Integer.valueOf(newProviderUrl.split(":")[1]);   // 端口
                    channelFutureWrapper.setPort(port);
                    channelFutureWrapper.setHost(host);
                    ChannelFuture channelFuture = null;     // netty连接
                    try {
                        channelFuture = ConnectionHandler.createChannelFuture(host,port);
                        channelFutureWrapper.setChannelFuture(channelFuture);
                        newChannelFutureWrapper.add(channelFutureWrapper);
                        finalUrl.add(newProviderUrl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            finalChannelFutureWrappers.addAll(newChannelFutureWrapper);
            //最终更新服务在这里
            CONNECT_MAP.put(urlChangeWrapper.getServiceName(),finalChannelFutureWrappers);
        }
    }
}
