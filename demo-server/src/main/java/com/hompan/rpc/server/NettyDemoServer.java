package com.hompan.rpc.server;

import com.hompan.rpc.main.annotation.ServiceScan;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcServer;
import com.hompan.rpc.main.transport.netty.server.NettyServer;

/**
 * @author PanHom
 * 测试用Netty服务提供者（服务端）
 */
@ServiceScan
public class NettyDemoServer {

    public static void main(String[] args) {
        //多态
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }

}
