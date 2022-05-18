package com.castor6.myrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import com.castor6.myrpc.framework.core.common.RpcDecoder;
import com.castor6.myrpc.framework.core.common.RpcEncoder;
import com.castor6.myrpc.framework.core.common.RpcProtocol;
import com.castor6.myrpc.framework.core.common.RpcRequest;
import com.castor6.myrpc.framework.core.common.config.ClientConfig;
import com.castor6.myrpc.framework.core.common.config.PropertiesBootstrap;
import com.castor6.myrpc.framework.core.common.event.MyRpcListenerLoader;
import com.castor6.myrpc.framework.core.common.util.CommonUtils;
import com.castor6.myrpc.framework.core.proxy.javassist.JavassistProxyFactory;
import com.castor6.myrpc.framework.core.proxy.jdk.JDKProxyFactory;
import com.castor6.myrpc.framework.core.registy.URL;
import com.castor6.myrpc.framework.core.registy.zookeeper.AbstractRegister;
import com.castor6.myrpc.framework.core.registy.zookeeper.ZookeeperRegister;
import com.castor6.myrpc.framework.interfaces.DataService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;
import static com.castor6.myrpc.framework.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;

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

    private Logger logger = LoggerFactory.getLogger(Client.class);

    public static EventLoopGroup clientGroup = new NioEventLoopGroup();

    private ClientConfig clientConfig;

    private AbstractRegister register;

    private MyRpcListenerLoader myRpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }


    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcFactory initClientApplication() {
        // netty相关初始化
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        // 监听器加载器相关初始化（监听器是负责处理某类型事件的，加载器负责加载这些监听器）
        myRpcListenerLoader = new MyRpcListenerLoader();
        myRpcListenerLoader.init();
        // 初始化客户端的配置
        this.clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        // 初始化代理方式
        RpcFactory rpcFactory;
        if ("javassist".equals(clientConfig.getProxyType())) {
            rpcFactory = new RpcFactory(new JavassistProxyFactory());
        } else {
            rpcFactory = new RpcFactory(new JDKProxyFactory());
        }
        return rpcFactory;
    }

    /**
     * 启动服务之前需要预先订阅对应的服务
     *
     * @param serviceBean
     */
    public void doSubscribeService(Class serviceBean) {
        if (register == null) {
            register = new ZookeeperRegister(clientConfig.getRegisterAddr());
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        register.subscribe(url);
    }

    /**
     * 开始和各个服务的所有provider建立连接，
     * 但具体是发送调用请求给服务的哪个provider就是负载均衡考虑的事情了
     * 至于说担心与所有的Provider都建立连接，会不会增加开销的负担
     * 若是要发送调用请求才建立连接，面对一些突发流量的话，容易导致调用堆积
     */
    public void doConnectServer() {
        for (String serviceName : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = register.getProviderIps(serviceName);
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(serviceName, providerIp);
                } catch (InterruptedException e) {
                    logger.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.setServiceName(serviceName);
            register.doAfterSubscribe(url);     // 监听服务提供者的动态上下线
        }
    }


    /**
     * 开启发送线程，专门负责发送调用请求
     *
     * @param
     */
    public void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }

    class AsyncSendJob implements Runnable {

        public AsyncSendJob() {
        }

        @Override
        public void run() {
            while (true) {  // 负责发送请求
                try {
                    //阻塞模式
                    RpcRequest data = SEND_QUEUE.take();
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getServiceName());    // 负载均衡获取服务提供方的连接
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcFactory rpcFactory = client.initClientApplication();
        DataService dataService = rpcFactory.get(DataService.class);
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        for (int i = 0; i < 100; i++) {
            try {
                String result = dataService.sendData("test");
                System.out.println(result);
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
