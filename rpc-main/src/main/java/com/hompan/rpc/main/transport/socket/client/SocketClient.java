package com.hompan.rpc.main.transport.socket.client;

import com.hompan.rpc.common.util.RpcMessageChecker;
import com.hompan.rpc.main.loadbalancer.LoadBalancer;
import com.hompan.rpc.main.loadbalancer.RandomLoadBalancer;
import com.hompan.rpc.main.registry.NacosServiceDiscovery;
import com.hompan.rpc.main.registry.ServiceDiscovery;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.RpcClient;
import com.hompan.rpc.main.transport.socket.util.ObjectReader;
import com.hompan.rpc.main.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hompan.rpc.common.message.CoapRequest;
import com.hompan.rpc.common.message.CoapResponse;
import com.hompan.rpc.common.enumeration.ResponseCode;
import com.hompan.rpc.common.enumeration.RpcError;
import com.hompan.rpc.common.exception.RpcException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author PanHom
 * Socket方式远程方法调用的消费者（客户端）
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;

    private final CommonSerializer serializer;

    public SocketClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }
    public SocketClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }
    public SocketClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }

    public SocketClient(Integer serializerCode, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializerCode);
    }

    @Override
    public Object sendRequest(CoapRequest CoapRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(CoapRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, CoapRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            CoapResponse CoapResponse = (CoapResponse) obj;
            if (CoapResponse == null) {
                logger.error("服务调用失败，service：{}", CoapRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + CoapRequest.getInterfaceName());
            }
            if (CoapResponse.getStatusCode() == null || CoapResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败, service: {}, response:{}", CoapRequest.getInterfaceName(), CoapResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + CoapRequest.getInterfaceName());
            }
            RpcMessageChecker.check(CoapRequest, CoapResponse);
            return CoapResponse;
        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

}
