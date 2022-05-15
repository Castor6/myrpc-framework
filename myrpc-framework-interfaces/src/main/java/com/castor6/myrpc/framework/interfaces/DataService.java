package com.castor6.myrpc.framework.interfaces;

import java.util.List;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:28
 * @Description
 */
public interface DataService {

    // 发送数据
    String sendData(String body);

    // 获取数据
    List<String> getList();
}