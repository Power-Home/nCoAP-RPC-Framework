package com.hompan.rpc.client;


import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcClientProxy;
import com.hompan.rpc.main.transport.socket.client.SocketClient;
import com.hompan.rpc.services.ByeService;
import com.hompan.rpc.services.HelloService;
import com.hompan.rpc.services.Info;

/**
 * @author PanHom
 * 测试用消费者（客户端）
 */
public class SocketDemoClient {

    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy proxyGetter = new RpcClientProxy(client);  //invocation handler
        //返回一个动态代理对象
        HelloService helloService = proxyGetter.getProxy(HelloService.class);
        Info clientInfo = new Info(666, "hom pan","Nice to meet you!");
        //执行实现类的服务方法（实际是利用动态代理发送一个请求coaprequest），并得到远程调用的结果
        String res = helloService.sayHi(clientInfo);
        System.out.println(res);

        ByeService byeService = proxyGetter.getProxy(ByeService.class);
        System.out.println(byeService.sayBye(new Info(777,"hom pan","Have a good day!")));
    }

}
