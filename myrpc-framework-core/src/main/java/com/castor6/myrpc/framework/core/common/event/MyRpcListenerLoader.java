package com.castor6.myrpc.framework.core.common.event;

import com.castor6.myrpc.framework.core.common.event.update.ServiceUpdateListener;
import com.castor6.myrpc.framework.core.common.util.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-17 17:56
 * @Description Loader可以理解为加载所有处理各自事件的监听器
 */
public class MyRpcListenerLoader {

    private static List<MyRpcListener> myRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(MyRpcListener myRpcListener) {
        myRpcListenerList.add(myRpcListener);
    }

    public void init() {    // 可供扩展，当之后有新的事件需求，那么就再编写一个处理该事件的监听器类，并注册到MyRpcListenerLoader中
        registerListener(new ServiceUpdateListener());
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     */
    public static Class<?> getInterfaceT(Object o) {    // 如o的类型是ServiceUpdateListener类
        Type[] types = o.getClass().getGenericInterfaces();     // 获取ServiceUpdateListener类实现的接口
        ParameterizedType parameterizedType = (ParameterizedType) types[0];     // 获取到了MyRpcListener接口
        Type type = parameterizedType.getActualTypeArguments()[0];  // 查看其实现的MyRpcListener接口上的泛型
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    public static void sendSyncEvent(MyRpcEvent myRpcEvent) {
        if (CommonUtils.isEmptyList(myRpcListenerList)) {
            return;
        }
        for (MyRpcListener<?> myRpcListener : myRpcListenerList) {
            Class<?> type = getInterfaceT(myRpcListener);
            if (type.equals(myRpcEvent.getClass())) {
                try {
                    myRpcListener.callBack(myRpcEvent.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendEvent(MyRpcEvent myRpcEvent) {
        if(CommonUtils.isEmptyList(myRpcListenerList)){
            return;
        }
        for (MyRpcListener<?> myRpcListener : myRpcListenerList) {  // 遍历监听器
            Class<?> type = getInterfaceT(myRpcListener);   // 获取监听器接口上的泛型，标识他能处理哪个类型的事件
            if(type.equals(myRpcEvent.getClass())){     // 泛型匹配当前事件，就交给监听器处理
                eventThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {     // 线程池中的线程执行监听器中处理事件的逻辑
                        try {
                            myRpcListener.callBack(myRpcEvent.getData());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

}
