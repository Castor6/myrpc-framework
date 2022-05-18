package com.castor6.myrpc.framework.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:08
 * @Description 编码器：负责将rpc协议报文转换为字节流发出去
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol msg, ByteBuf out) throws Exception {
        // 由于已知协议的数据格式，就不需要重复将协议序列化，而是按照固定格式直接将字段转换为字节流发出去
        out.writeShort(msg.getMagicNumber());   // 魔数
        out.writeInt(msg.getPackageType());     // 协议体类型（请求/响应）
        out.writeInt(msg.getSerializerCode());  // 协议体的序列化方式
        out.writeInt(msg.getContentLength());   // 实际协议体数据长度
        out.writeBytes(msg.getContent());       // 实际协议体数据（调用请求或响应）
    }
}
