package com.castor6.myrpc.framework.core.common.event.update;

import lombok.Data;

import java.util.List;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-17 18:04
 * @Description 负责装填在更新事件中的数据
 */
@Data
public class URLChangeWrapper {

    private String serviceName;     // 服务名

    private List<String> providerUrl;   // 服务的新的路径集合

}
