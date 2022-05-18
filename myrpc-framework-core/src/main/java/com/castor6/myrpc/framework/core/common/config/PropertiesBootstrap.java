package com.castor6.myrpc.framework.core.common.config;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-17 18:25
 * @Description 客户端和服务端先暂时共用一个配置文件
 */

import java.io.IOException;

public class PropertiesBootstrap {

    private volatile boolean configIsReady;

    public static final String SERVER_PORT = "myrpc.serverPort";
    public static final String REGISTER_ADDRESS = "myrpc.registerAddr";
    public static final String APPLICATION_NAME = "myrpc.applicationName";
    public static final String PROXY_TYPE = "myrpc.proxyType";
    public static final String ROUTE = "myrpc.router";
    public static final String SERVER_SERIALIZE_TYPE = "myrpc.serverSerialize";
    public static final String CLIENT_SERIALIZE_TYPE = "myrpc.clientSerialize";

    public static ServerConfig loadServerConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail,e is {}", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(PropertiesLoader.getPropertiesInteger(SERVER_PORT));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        serverConfig.setServerSerialize(PropertiesLoader.getPropertiesStr(SERVER_SERIALIZE_TYPE));
        return serverConfig;
    }

    public static ClientConfig loadClientConfigFromLocal(){
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadClientConfigFromLocal fail,e is {}", e);
        }
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStr(PROXY_TYPE));
        clientConfig.setRouterStrategy(PropertiesLoader.getPropertiesStr(ROUTE));
        clientConfig.setClientSerialize(PropertiesLoader.getPropertiesStr(CLIENT_SERIALIZE_TYPE));
        return clientConfig;
    }


}