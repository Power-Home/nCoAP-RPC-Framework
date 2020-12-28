package com.hompan.rpc.common.exception;

import com.hompan.rpc.common.enumeration.RpcError;

/**
 * RPC调用异常
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }

}
