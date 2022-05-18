package com.castor6.myrpc.framework.core.registy.zookeeper;

import com.castor6.myrpc.framework.core.registy.RegistryService;
import com.castor6.myrpc.framework.core.registy.URL;

import java.util.List;
import java.util.Map;

import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static com.castor6.myrpc.framework.core.common.cache.CommonServerCache.PROVIDER_URL_SET;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 23:05
 * @Description 抽象类提供对注册中心接口的一些默认实现（主要是更新相关的本地缓存），因为考虑到注册中心可以由redis或zookeeper或nacos实现
 */
public abstract class AbstractRegister implements RegistryService {

    // Provider（服务提供方）上线后注册服务
    @Override
    public void register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    // Provider（服务提供方）下线后取消注册服务
    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    // Consumer（服务调用方）订阅服务
    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url.getServiceName());
    }

    @Override
    public void unSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param serviceName
     * @return
     */
    public abstract List<String> getProviderIps(String serviceName);
}
