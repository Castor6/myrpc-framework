package com.castor6.myrpc.framework.core.common.cache;

import com.castor6.myrpc.framework.core.registy.RegistryService;
import com.castor6.myrpc.framework.core.registy.URL;
import io.netty.util.internal.ConcurrentSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:26
 * @Description 服务端缓存
 */
public class CommonServerCache {
    public static final Map<String,Object> PROVIDER_CLASS_MAP = new ConcurrentHashMap<>();  // 本地服务注册中心（接口名 -> 提供服务的接口实现类的单例对象）
    public static final Set<URL> PROVIDER_URL_SET = new ConcurrentSet<>();  // 缓存本地能提供的服务
    public static RegistryService REGISTRY_SERVICE;
//    public static SerializeFactory SERVER_SERIALIZE_FACTORY;
}
