package com.hompan.rpc.main.transport;

import com.hompan.rpc.common.message.CoapRequest;
import com.hompan.rpc.main.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(CoapRequest rpcRequest);

}
