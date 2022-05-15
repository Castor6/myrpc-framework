package com.castor6.myrpc.framework.core.server;

import com.alibaba.fastjson.JSON;
import com.castor6.myrpc.framework.core.common.ResponseCode;
import com.castor6.myrpc.framework.core.common.RpcRequest;
import com.castor6.myrpc.framework.core.common.RpcProtocol;
import com.castor6.myrpc.framework.core.common.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.castor6.myrpc.framework.core.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:17
 * @Description 服务端处理接收到的rpc协议报文
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    public static final RequestHandler REQUEST_HANDLER = new RequestHandler();  // 单例模式饿汉式

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //服务端接收数据的时候统一以RpcProtocol协议的格式接收，具体的发送逻辑见文章下方客户端发送部分
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        String json = new String(rpcProtocol.getContent(), 0, rpcProtocol.getContentLength());  // 得到协议体
        RpcRequest rpcRequest = JSON.parseObject(json, RpcRequest.class);  // 协议体其实是一个json字符串，转成rpcRequest对象
        // 交由处理器处理请求，返回的是响应报文
        RpcResponse rpcResponse = REQUEST_HANDLER.handle(rpcRequest);
        rpcResponse.setUuid(rpcRequest.getUuid());     // 将请求报文中的uuid注入回去

        RpcProtocol respRpcProtocol = new RpcProtocol(JSON.toJSONString(rpcResponse).getBytes());
        RpcResponse response = JSON.parseObject(JSON.toJSONString(rpcResponse), RpcResponse.class);


        ctx.writeAndFlush(respRpcProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
