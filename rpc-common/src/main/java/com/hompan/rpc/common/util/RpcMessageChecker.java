package com.hompan.rpc.common.util;

import com.hompan.rpc.common.message.CoapRequest;
import com.hompan.rpc.common.message.CoapResponse;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hompan.rpc.common.enumeration.ResponseCode;
import com.hompan.rpc.common.enumeration.RpcError;
import com.hompan.rpc.common.exception.RpcException;

/**
 * 检查响应与请求
 */
@NoArgsConstructor
public class RpcMessageChecker {

    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    public static void check(CoapRequest rpcRequest, CoapResponse CoapResponse) {
        if (CoapResponse == null) {
            logger.error("调用服务失败,serviceName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(CoapResponse.getRequestId())) {
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (CoapResponse.getStatusCode() == null || !CoapResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            logger.error("调用服务失败,serviceName:{},CoapResponse:{}", rpcRequest.getInterfaceName(), CoapResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }

}
