package com.castor6.myrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import com.castor6.myrpc.framework.core.common.RpcDecoder;
import com.castor6.myrpc.framework.core.common.RpcEncoder;
import com.castor6.myrpc.framework.core.common.RpcRequest;
import com.castor6.myrpc.framework.core.common.RpcProtocol;
import com.castor6.myrpc.framework.core.config.ClientConfig;
import com.castor6.myrpc.framework.core.proxy.jdk.JDKProxyFactory;
import com.castor6.myrpc.framework.interfaces.DataService;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.castor6.myrpc.framework.core.cache.CommonClientCache.SEND_QUEUE;
import static com.castor6.myrpc.framework.core.constants.RpcConstants.DEFAULT_DECODE_CHAR;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 17:13
 * @Description 客户端首先需要通过一个代理工厂获取被调用对象的代理对象，
 *          然后通过代理对象将数据放入发送队列，
 *          最后会有一个异步线程将发送队列内部的数据一个个地发送给到服务端，
 *          并且等待服务端响应对应的数据结果。
 *          所以这里最为核心的思想就是：
 *          将请求发送任务交给单独的IO线程区负责，实现异步化，提升发送性能。
 */
public class Client {

    public static EventLoopGroup clientGroup = new NioEventLoopGroup();
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private ClientConfig clientConfig;

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcFactory startClientApplication() throws InterruptedException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //管道中初始化一些逻辑，这里包含了上边所说的编解码器和客户端响应类
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        //常规的链接netty服务端
        ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddr(), clientConfig.getPort()).sync();
        logger.info("============ 服务启动 ============");
        this.startClient(channelFuture);
        //这里注入了一个代理工厂，这个代理类在下文会仔细介绍
        RpcFactory rpcReference = new RpcFactory(new JDKProxyFactory());
        return rpcReference;
    }


    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setPort(9090);
        clientConfig.setServerAddr("localhost");
        client.setClientConfig(clientConfig);
        RpcFactory rpcReference = client.startClientApplication();
        DataService dataService = rpcReference.get(DataService.class);
        String result = dataService.sendData("test");
        System.out.println(result);
//        for(int i=0;i<100;i++){
//
//        }
    }

    /**
     * 开启发送线程，专门从事将数据包发送给服务端，起到一个解耦的效果
     * @param channelFuture
     */
    private void startClient(ChannelFuture channelFuture) {
        Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
        asyncSendJob.start();
    }

    /**
     * 异步发送信息任务
     *
     */
    class AsyncSendJob implements Runnable {

        private ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞模式
                    RpcRequest data = SEND_QUEUE.take();
                    //将rpcRequest封装到RpcProtocol对象中，然后发送给服务端，这里正好对应了上文中的ServerHandler
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());

                    //netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
