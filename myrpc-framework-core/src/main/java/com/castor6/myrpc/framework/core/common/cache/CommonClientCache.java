package com.castor6.myrpc.framework.core.common.cache;

import com.castor6.myrpc.framework.core.common.ChannelFutureWrapper;
import com.castor6.myrpc.framework.core.common.RpcRequest;
import com.castor6.myrpc.framework.core.common.config.ClientConfig;
import com.castor6.myrpc.framework.core.registy.URL;
import com.castor6.myrpc.framework.core.router.MyRouter;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:26
 * @Description 客户端缓存
 */
public class CommonClientCache {
    public static BlockingQueue<RpcRequest> SEND_QUEUE = new ArrayBlockingQueue(5000);  // 发送阻塞队列
    public static Map<String,Object> RESP_MAP = new ConcurrentHashMap<>();  // key是与请求一一对应的uuid，value用于存放与请求对应的响应，初始还未收到响应时存放一个共同的Object类对象

    public static ClientConfig CLIENT_CONFIG;   // 客户端的配置

    public static List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();  // 存放订阅的所有服务（接口）名
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();  // 服务名称 --> 已与服务提供者建立的连接（实际使用的缓存这个比较重要）

    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();   // 服务名称 --> 该服务有哪些集群URL（包括应用名称、ip、端口号、权重（应该是路由层使用的东西）等）
    public static Set<String> SERVER_ADDRESS = new HashSet<>();     // 记录已连接的服务提供者的地址（ip地址:端口）

    public static MyRouter MYROUTER;
}
