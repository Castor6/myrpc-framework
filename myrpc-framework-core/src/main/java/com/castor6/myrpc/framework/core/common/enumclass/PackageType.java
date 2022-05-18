package com.castor6.myrpc.framework.core.common.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 19:43
 * @Description 数据包的枚举类：请求/响应
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
