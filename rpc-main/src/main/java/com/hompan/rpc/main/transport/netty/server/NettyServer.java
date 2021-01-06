package com.hompan.rpc.main.transport.netty.server;

import com.hompan.rpc.main.codec.CommonDecoder;
import com.hompan.rpc.main.codec.CommonEncoder;
import com.hompan.rpc.main.hook.ShutdownHook;
import com.hompan.rpc.main.provider.ServiceProviderImpl;
import com.hompan.rpc.main.registry.NacosServiceRegistry;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.AbstractRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author PanHom
 * NIO方式服务提供侧
 */
public class NettyServer extends AbstractRpcServer {

    private final CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry(); //Nacos服务注册中心
        serviceProvider = new ServiceProviderImpl(); //默认的服务注册表，保存服务端本地服务
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices(); //扫描注解，并向nacos以及服务器注册服务
    }

    @Override
    public void start() {
        //注册钩子函数--关闭后将自动注销注册中心的所有服务,以及关闭线程池
        ShutdownHook.getShutdownHook().addClearAllHook();
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动引导类，串联各个组件，并配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //设置channel实现类（反射）
                    .handler(new LoggingHandler(LogLevel.INFO)) //添加日志处理器
                    .option(ChannelOption.SO_BACKLOG, 256) //设置线程队列得到的连接个数
                    .option(ChannelOption.SO_KEEPALIVE, true) //保持活跃连接
                    .childOption(ChannelOption.TCP_NODELAY, true) //是否尽量发大包
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个通道测试对象
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipelne添加handler链（可自定义handler）,处理消息
                            /**
                             * handler链中每一个handler产生的事件event会由下一个handler的userEventTriggered方法处理，
                             * 事件可顺延？
                             */                //空闲状态处理器，一定时间没有读或写则发送心跳检测包检测连接
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new CommonEncoder(serializer))
                                    .addLast(new CommonDecoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });
            //绑定一个端口并同步，立刻返回一个ChannelFuture对象，此时服务器即启动（异步）
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            //对关闭通道事件进行监听future listener（异步）
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
