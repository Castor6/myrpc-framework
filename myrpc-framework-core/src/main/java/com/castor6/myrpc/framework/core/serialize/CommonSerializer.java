package com.castor6.myrpc.framework.core.serialize;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-18 20:00
 * @Description 序列化层，提供序列化/反序列化方法
 */
public interface CommonSerializer {
    <T> byte[] serialize(T t);
    <T> T deserialize(byte[] data, Class<T> clazz);
}
