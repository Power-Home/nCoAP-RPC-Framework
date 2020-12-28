package com.hompan.rpc.client;


import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcClientProxy;
import com.hompan.rpc.main.transport.socket.client.SocketClient;
import com.hompan.rpc.services.ByeService;
import com.hompan.rpc.services.HelloService;
import com.hompan.rpc.services.Info;

/**
 * 测试用消费者（客户端）
 */
public class SocketDemoClient {

    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        Info clientInfo = new Info(666, "hom pan","Nice to meet you!");
        String res = helloService.sayHi(clientInfo);
        System.out.println(res);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.sayBye(new Info(777,"hom pan","Have a good day!")));
    }

}
