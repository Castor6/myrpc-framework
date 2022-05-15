package com.castor6.myrpc.framework.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.castor6.myrpc.framework.core.constants.RpcConstants.MAGIC_NUMBER;


/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-13 23:12
 * @Description 解码器：负责处理将收到的字节流转换为rpc协议报文
 *              通过自定义协议中的数据包长度字段，
 *              解决了TCP的粘包（多个数据包合在一起发送）和拆包（数据包的剩余部分在另一个数据包中）
 */
public class RpcDecoder extends ByteToMessageDecoder {
    /**
     * 协议的开头部分的标准长度
     */
    public final int BASE_LENGTH = 2 + 4;   // 2字节魔数加4字节长度

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            //防止收到一些体积过大的数据包
            int beginReader;
            while (true) {
                // 标记协议的开头，用以解决拆包问题，能让当前请求剩余的下一次的数据包获取时能再次根据读指针获取请求的内容长度
                beginReader = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                //这里对应了RpcProtocol的魔数
                if (byteBuf.readShort() == MAGIC_NUMBER) {
                    break;
                } else {
                    // 不是魔数开头，说明是非法的客户端发来的数据包
                    ctx.close();
                    return;
                }
            }
            //这里对应了RpcProtocol对象的contentLength字段
            int length = byteBuf.readInt();
            //说明剩余的数据包不是完整的，这里需要重置下读索引
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);
                return;
            }
            //这里其实就是实际的RpcProtocol对象的content字段
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            RpcProtocol rpcProtocol = new RpcProtocol(data);
            out.add(rpcProtocol);
        }
    }
}
