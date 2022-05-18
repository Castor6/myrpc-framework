package com.castor6.myrpc.framework.core.common.event.nodedatachange;

import com.castor6.myrpc.framework.core.common.ChannelFutureWrapper;
import com.castor6.myrpc.framework.core.common.event.MyRpcListener;
import com.castor6.myrpc.framework.core.registy.URL;
import com.castor6.myrpc.framework.core.registy.zookeeper.ProviderNodeInfo;

import java.util.List;

import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.MYROUTER;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 16:13
 * @Description
 */
public class ProviderNodeDataChangeListener implements MyRpcListener<MyRpcNodeDataChangeEvent> {

    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureWrapper> channelFutureWrappers =  CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            //重置分组信息
            String address = channelFutureWrapper.getHost()+":"+channelFutureWrapper.getPort();
            if(address.equals(providerNodeInfo.getAddress())){
                channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
                //修改权重
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                //更新权重
//                MYROUTER.updateWeight(url);
                break;
            }
        }
    }
}