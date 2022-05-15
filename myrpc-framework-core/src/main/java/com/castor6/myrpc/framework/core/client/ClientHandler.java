package com.castor6.myrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import com.castor6.myrpc.framework.core.common.ResponseCode;
import com.castor6.myrpc.framework.core.common.RpcResponse;
import com.castor6.myrpc.framework.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static com.castor6.myrpc.framework.core.cache.CommonClientCache.RESP_MAP;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 17:25
 * @Description 负责接收调用服务的响应（读入相当于输入流）
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //客户端和服务端之间的数据都是以RpcProtocol对象作为基本协议进行的交互
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        //取出响应报文
        byte[] reqContent = rpcProtocol.getContent();
        String json = new String(reqContent,0,reqContent.length);
        RpcResponse rpcResponse = JSON.parseObject(json, RpcResponse.class);
        //检查方法是否执行成功
        if(rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
            rpcResponse.getThrowable().printStackTrace();
        }
        //通过之前发送的uuid来注入匹配的响应数值
        if(!RESP_MAP.containsKey(rpcResponse.getUuid())){
            throw new IllegalArgumentException("server response is error!");
        }
        //将请求的响应结构放入一个Map集合中，集合的key就是uuid，这个uuid在发送请求之前就已经初始化好了，所以只需要起一个线程在后台遍历这个map，查看对应的key是否有相应即可。
        RESP_MAP.put(rpcResponse.getUuid(),rpcResponse);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
