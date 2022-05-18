package com.castor6.myrpc.framework.core.common.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 20:13
 * @Description
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    KRYO(0),
    FAJSON(1),
    JDK(2);
    private final int code;
}
