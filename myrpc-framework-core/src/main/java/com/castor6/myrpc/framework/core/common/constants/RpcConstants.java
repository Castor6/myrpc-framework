package com.castor6.myrpc.framework.core.common.constants;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-05-14 16:34
 * @Description
 */
public class RpcConstants {
    public static final short MAGIC_NUMBER = (short) 0xCAFE;
    public static final String DEFAULT_DECODE_CHAR = "$_i0#Xsop1_$";
    // 路由策略
    public static final String RANDOM_ROUTER_TYPE = "random";
    public static final String ROTATE_ROUTER_TYPE = "rotate";
    // 序列化方式
    public static final String KRYO_SERIALIZE_TYPE = "kryo";
    public static final String FAST_JSON_SERIALIZE_TYPE = "fastJson";
    public static final String JDK_SERIALIZE_TYPE = "jdk";

}
