package com.castor6.myrpc.framework.core.cache;

import com.castor6.myrpc.framework.core.common.RpcRequest;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:26
 * @Description 客户端的缓存包括
 */
public class CommonClientCache {
    public static BlockingQueue<RpcRequest> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String,Object> RESP_MAP = new ConcurrentHashMap<>();
}
