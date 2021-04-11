package com.hompan.rpc.main.transport.socket.server;

import com.hompan.rpc.main.handler.RequestHandler;
import com.hompan.rpc.main.serializer.CommonSerializer;
import com.hompan.rpc.main.transport.socket.util.ObjectReader;
import com.hompan.rpc.main.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hompan.rpc.common.message.CoapRequest;
import com.hompan.rpc.common.message.CoapResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author PanHom
 * 服务器端处理CoapRequest的工作线程，从线程池取出
 */
public class SocketRequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SocketRequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            CoapRequest CoapRequest = (CoapRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handle(CoapRequest);  //调用注册的实现类方法去处理
            CoapResponse<Object> response = CoapResponse.success(result, CoapRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer);
        } catch (IOException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

}
