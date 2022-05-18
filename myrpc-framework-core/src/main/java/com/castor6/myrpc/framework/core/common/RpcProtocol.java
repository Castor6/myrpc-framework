package com.castor6.myrpc.framework.core.common;

import com.castor6.myrpc.framework.core.common.enumclass.PackageType;
import com.castor6.myrpc.framework.core.common.enumclass.SerializerCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import static com.castor6.myrpc.framework.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-13 22:56
 * @Description 封装调用请求或响应的rpc协议
 */
@Data
@NoArgsConstructor
@ToString
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 5359096060555795690L;

    private short magicNumber = MAGIC_NUMBER;   // 魔数，标识该数据包是rpc协议

    private int packageType;    // 协议体类型（请求/响应）

    private int serializerCode;  // 协议体的序列化方式

    private int contentLength;  // 实际协议体数据长度

    private byte[] content; // 协议体：调用请求或响应

    public RpcProtocol(byte[] content, int packageType, int serializerCode) {
        this.contentLength = content.length;
        this.content = content;
        this.packageType = packageType;
        this.serializerCode = serializerCode;
    }
}