package com.castor6.myrpc.framework.core.registy.zookeeper;

import lombok.Data;
import lombok.ToString;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 23:12
 * @Description 服务节点
 */
@Data
@ToString
public class ProviderNodeInfo {
    private String applicationName;

    private String serviceName;

    private String address;

    private Integer weight;

    private String registryTime;

    private String group;

}
