package com.castor6.myrpc.framework.core.registy.zookeeper;

import lombok.Data;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 23:09
 * @Description 操作Zookeeper的抽象类，因为考虑到可以选用zkclient或curator其中一个客户端来操作Zookeeper
 */
@Data
public abstract class AbstractZookeeperClient {

    private String zkAddress;
    private int baseSleepTimes;
    private int maxRetryTimes;

    public AbstractZookeeperClient(String zkAddress) {
        this.zkAddress = zkAddress;
        //默认3000ms
        this.baseSleepTimes = 1000;
        this.maxRetryTimes = 3;
    }

    public AbstractZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetryTimes) {
        this.zkAddress = zkAddress;
        if (baseSleepTimes == null) {
            this.baseSleepTimes = 1000;
        } else {
            this.baseSleepTimes = baseSleepTimes;
        }
        if (maxRetryTimes == null) {
            this.maxRetryTimes = 3;
        } else {
            this.maxRetryTimes = maxRetryTimes;
        }
    }

    public abstract void updateNodeData(String address, String data);

    public abstract Object getClient();

    /**
     * 拉取节点的数据
     *
     * @param path
     * @return
     */
    public abstract String getNodeData(String path);

    /**
     * 获取指定目录下的子节点数据
     *
     * @param path 虽然也指的是节点，当由于我们是要获取该节点下的子节点，所以称之为路径
     * @return
     */
    public abstract List<String> getChildrenData(String path);

    /**
     * 创建持久化类型节点数据信息
     *
     * @param address 指的是具体某个节点
     * @param data
     */
    public abstract void createPersistentData(String address, String data);

    /**
     * @param address
     * @param data
     */
    public abstract void createPersistentWithSeqData(String address, String data);


    /**
     * 创建有序且临时类型节点数据信息
     *
     * @param address
     * @param data
     */
    public abstract void createTemporarySeqData(String address, String data);


    /**
     * 创建临时节点数据类型信息
     *
     * @param address
     * @param data
     */
    public abstract void createTemporaryData(String address, String data);

    /**
     * 设置某个节点的数值
     *
     * @param address
     * @param data
     */
    public abstract void setTemporaryData(String address, String data);

    /**
     * 断开zk的客户端链接
     */
    public abstract void destroy();


    /**
     * 展示节点下边的数据
     *
     * @param address
     */
    public abstract List<String> listNode(String address);


    /**
     * 删除节点下边的数据
     *
     * @param address
     * @return
     */
    public abstract boolean deleteNode(String address);


    /**
     * 判断是否存在其他节点
     *
     * @param address
     * @return
     */
    public abstract boolean existNode(String address);


    /**
     * 监听path路径下某个节点的数据变化
     *
     * @param path
     */
    public abstract void watchNodeData(String path, Watcher watcher);

    /**
     * 监听子节点下的数据变化
     *
     * @param path
     * @param watcher
     */
    public abstract void watchChildNodeData(String path, Watcher watcher);

}
