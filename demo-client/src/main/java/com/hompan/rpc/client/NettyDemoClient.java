package com.hompan.rpc.client;


import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcClient;
import com.hompan.rpc.main.transport.RpcClientProxy;
import com.hompan.rpc.main.transport.netty.client.NettyClient;
import com.hompan.rpc.services.ByeService;
import com.hompan.rpc.services.HelloService;
import com.hompan.rpc.services.Info;

/**
 * 测试用Netty消费者
 * proxy actually means procy factory here, which aims to create a corresponding dynamic proxy instance here
 */
public class NettyDemoClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client); //RPC客户端动态代理
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Info clientInfo = new Info(666, "hom pan","Nice to meet you!");
        String res = helloService.sayHi(clientInfo);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.sayBye(new Info(777,"hom pan","Have a good day!")));
    }

}
