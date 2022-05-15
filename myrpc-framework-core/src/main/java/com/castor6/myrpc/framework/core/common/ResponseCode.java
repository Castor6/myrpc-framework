package com.castor6.myrpc.framework.core.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 15:14
 * @Description 状态码的枚举类
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200,"调用方法成功"),
    FAIL(500,"调用方法失败"),
    NOT_FOUND_METHOD(404,"未找到指定方法"),
    NOT_FOUND_CLASS(404,"未找到指定类");

    private final int code;
    private final String message;

}
