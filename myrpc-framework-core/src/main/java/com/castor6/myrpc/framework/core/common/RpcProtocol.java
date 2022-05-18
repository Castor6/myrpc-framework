package com.castor6.myrpc.framework.core.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import static com.castor6.myrpc.framework.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-13 22:56
 * @Description
 */
@Data
@NoArgsConstructor
@ToString
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 5359096060555795690L;

    private short magicNumber = MAGIC_NUMBER;

    private int contentLength;

    private byte[] content;

    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }
}