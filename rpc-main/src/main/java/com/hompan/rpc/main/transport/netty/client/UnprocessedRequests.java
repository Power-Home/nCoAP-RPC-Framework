package com.hompan.rpc.main.transport.netty.client;

import com.hompan.rpc.common.message.CoapResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<CoapResponse>> unprocessedResponseFutures = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<CoapResponse> future) {
        unprocessedResponseFutures.put(requestId, future);
    }

    public void remove(String requestId) {
        unprocessedResponseFutures.remove(requestId);
    }

    public void complete(CoapResponse CoapResponse) {
        CompletableFuture<CoapResponse> future = unprocessedResponseFutures.remove(CoapResponse.getRequestId());
        if (null != future) {
            future.complete(CoapResponse);
        } else {
            throw new IllegalStateException();
        }
    }

}
