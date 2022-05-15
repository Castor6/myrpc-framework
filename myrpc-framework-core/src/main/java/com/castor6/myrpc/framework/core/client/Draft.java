package com.castor6.myrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import com.castor6.myrpc.framework.core.common.RpcResponse;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-15 20:14
 * @Description
 */
public class Draft {
    public static void main(String[] args) {
        Throwable throwable = null;
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            throwable = e;
        }
        String json = JSON.toJSONString(throwable);
        Throwable throwable1 = JSON.parseObject(json, Throwable.class);
        throwable1.printStackTrace();
    }
}
