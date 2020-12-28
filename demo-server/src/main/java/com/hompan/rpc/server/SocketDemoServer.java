package com.hompan.rpc.server;

import com.hompan.rpc.main.annotation.ServiceScan;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcServer;
import com.hompan.rpc.main.transport.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 */
@ServiceScan
public class SocketDemoServer {

    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        server.start();
    }

}
