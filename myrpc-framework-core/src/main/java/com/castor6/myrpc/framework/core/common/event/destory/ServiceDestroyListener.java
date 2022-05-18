package com.castor6.myrpc.framework.core.common.event.destory;

import com.castor6.myrpc.framework.core.common.event.MyRpcListener;
import com.castor6.myrpc.framework.core.registy.URL;

import static com.castor6.myrpc.framework.core.common.cache.CommonServerCache.PROVIDER_URL_SET;
import static com.castor6.myrpc.framework.core.common.cache.CommonServerCache.REGISTRY_SERVICE;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 16:47
 * @Description 处理服务提供方下线前将所有注册过的服务注销掉
 */
public class ServiceDestroyListener implements MyRpcListener<MyRpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}