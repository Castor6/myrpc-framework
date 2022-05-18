package com.castor6.myrpc.framework.core.common.config;

import lombok.Data;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:23
 * @Description
 */
@Data
public class ServerConfig {
    private Integer serverPort;

    private String registerAddr;

    private String applicationName;

    /**
     * 服务端序列化方式 example: hession2,kryo,jdk,fastjson
     */
    private String serverSerialize;
}
