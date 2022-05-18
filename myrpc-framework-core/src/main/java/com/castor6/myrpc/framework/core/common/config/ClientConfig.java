package com.castor6.myrpc.framework.core.common.config;

import lombok.Data;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:24
 * @Description 客户端的配置类
 */
@Data
public class ClientConfig {

    private String applicationName;

    private String registerAddr;

    /**
     * 代理类型 example: jdk,javassist
     */
    private String proxyType;

    /**
     * 负载均衡策略 example:random,rotate
     */
    private String routerStrategy;

    /**
     * 客户端序列化方式 example: hession2,kryo,jdk,fastjson
     */
    private String clientSerialize;
}
