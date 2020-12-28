package com.hompan.rpc.main.transport;

import com.hompan.rpc.main.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 */
public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    <T> void publishService(T service, String serviceName);

}
