package com.hompan.rpc.main.transport.socket.server;

import com.hompan.rpc.common.factory.ThreadPoolFactory;
import com.hompan.rpc.main.handler.RequestHandler;
import com.hompan.rpc.main.hook.ShutdownHook;
import com.hompan.rpc.main.provider.ServiceProviderImpl;
import com.hompan.rpc.main.registry.NacosServiceRegistry;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.AbstractRpcServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author PanHom
 * Socket方式远程方法调用的提供者（服务端）
 */
public class SocketServer extends AbstractRpcServer {

    private final ExecutorService threadPool;
    private final CommonSerializer serializer;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server",false);
        this.serviceRegistry = new NacosServiceRegistry();  //nacos端
        this.serviceProvider = new ServiceProviderImpl();   //服务器端
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices(); //先扫描注册服务
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务器启动……");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:", e);
        }
    }

}
