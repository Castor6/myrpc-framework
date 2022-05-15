package com.castor6.myrpc.framework.core.config;

import lombok.Data;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:24
 * @Description
 */
@Data
public class ClientConfig {

    private Integer port;

    private String serverAddr;
}
