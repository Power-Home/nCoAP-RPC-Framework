package com.hompan.rpc.server;

import com.hompan.rpc.main.annotation.ServiceScan;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcServer;
import com.hompan.rpc.main.transport.socket.server.SocketServer;

/**
 * @author PanHom
 * 测试用服务提供方（服务端）
 * 注解形式注册、扫描服务实例
 */

@ServiceScan
public class SocketDemoServer {

    public static void main(String[] args) {
        //1.扫描服务，并注册进nacos和服务器中
        RpcServer server = new SocketServer("127.0.0.1", 6969, CommonSerializer.KRYO_SERIALIZER);
        //2.监听端口，处理连接请求并返回
        server.start();
    }

}
