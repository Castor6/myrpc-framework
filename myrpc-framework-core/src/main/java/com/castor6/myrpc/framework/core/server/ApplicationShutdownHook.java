package com.castor6.myrpc.framework.core.server;

import com.castor6.myrpc.framework.core.common.event.MyRpcListenerLoader;
import com.castor6.myrpc.framework.core.common.event.destory.MyRpcDestroyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 17:09
 * @Description 钩子在jvm关闭前将所有注册过的服务注销掉
 */
public class ApplicationShutdownHook {
    public static Logger LOGGER = LoggerFactory.getLogger(ApplicationShutdownHook.class);

    public static void registryShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("[registryShutdownHook] ==== ");
                MyRpcListenerLoader.sendSyncEvent(new MyRpcDestroyEvent("destroy"));
                System.out.println("destory");
            }
        }));
    }
}
