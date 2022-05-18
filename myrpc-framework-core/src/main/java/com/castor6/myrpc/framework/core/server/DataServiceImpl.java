package com.castor6.myrpc.framework.core.server;

import com.castor6.myrpc.framework.interfaces.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 18:44
 * @Description
 */
public class DataServiceImpl implements DataService {

    @Override
    public String sendData(String body) {
        System.out.println("己收到的参数长度："+body.length());
        return "success";
    }

    @Override
    public List<String> getList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("idea1");
        arrayList.add("idea2");
        arrayList.add("idea3");
        return arrayList;
    }
}