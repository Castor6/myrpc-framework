package com.castor6.myrpc.framework.core.serialize;

import com.alibaba.fastjson.JSON;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 20:08
 * @Description fastjson序列化器，通过将对象转为一个个键值对的json字符串实现序列化
 */
public class FastJsonSerializer implements CommonSerializer {

    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }

}
