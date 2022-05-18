package com.castor6.myrpc.framework.core.registy.zookeeper;

import com.castor6.myrpc.framework.core.common.event.MyRpcEvent;
import com.castor6.myrpc.framework.core.common.event.MyRpcListenerLoader;
import com.castor6.myrpc.framework.core.common.event.update.MyRpcUpdateEvent;
import com.castor6.myrpc.framework.core.common.event.update.URLChangeWrapper;
import com.castor6.myrpc.framework.core.registy.URL;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 23:11
 * @Description
 */
public class ZookeeperRegister extends AbstractRegister {

    private AbstractZookeeperClient zkClient;

    private String ROOT = "/myrpc";

    // ProviderPath： /myrpc/服务名/provider/host(ip):port
    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    // ConsumerPath： /myrpc/服务名/consumer/applicationName:host(ip):（:用于分割顺序节点的后缀）
    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParameters().get("host")+":";
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }


    @Override
    public List<String> getProviderIps(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        return nodeDataList;
    }

    @Override
    public void register(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildProviderUrlStr(url);   // 存储在节点中的数据
        if (!zkClient.existNode(getProviderPath(url))) {
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        } else {
            zkClient.deleteNode(getProviderPath(url));
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        }
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        zkClient.deleteNode(getProviderPath(url));
        super.unRegister(url);
    }

    @Override
    public void subscribe(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildConsumerUrlStr(url);
        if (!zkClient.existNode(getConsumerPath(url))) {
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        } else {
            zkClient.deleteNode(getConsumerPath(url));
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        }
        super.subscribe(url);
    }

    @Override
    public void doAfterSubscribe(URL url) {
        //监听是否有新的服务注册
        String newServerNodePath = ROOT + "/" + url.getServiceName() + "/provider";
        watchChildNodeData(newServerNodePath);
    }

    public void watchChildNodeData(String newServerNodePath){
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent);
                String path = watchedEvent.getPath();   // 监听服务路径：/myrpc/服务名/provider
                System.out.println(path);
                List<String> childrenDataList = zkClient.getChildrenData(path);     // 获取所有Provider所在的一层路径：host(ip):port
                System.out.println(childrenDataList);
                // 构造更新事件的数据
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(path.split("/")[2]);    // 啊这个在我测试时路径是/path，业务代码会出数组越界异常但是被curator捕获了，以至于控制台不能看到出现的异常
                // 构造更新事件，并交给专门的线程异步处理
                // 这里之所以不像发送请求那样丢到一个阻塞队列中，是因为更新事件要让对应的监听器处理，直接丢入阻塞队列中，不好实现
                MyRpcEvent myRpcEvent = new MyRpcUpdateEvent(urlChangeWrapper);
                MyRpcListenerLoader.sendEvent(myRpcEvent);      // 深入怎么异步处理更新服务的服务提供者连接列表
                // 收到回调之后再注册一次监听，这样能保证一直都收到通知
                watchChildNodeData(path);
            }
        });
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public void unSubscribe(URL url) {
        this.zkClient.deleteNode(getConsumerPath(url));
        super.unSubscribe(url);
    }

}
