package com.hompan.rpc.server;

import com.hompan.rpc.main.annotation.ServiceScan;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcServer;
import com.hompan.rpc.main.transport.netty.server.NettyServer;

/**
 * 测试用Netty服务提供者（服务端）
 */
@ServiceScan
public class NettyDemoServer {

    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }

}
