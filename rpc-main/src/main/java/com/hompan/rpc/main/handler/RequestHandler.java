package com.hompan.rpc.main.handler;

import com.hompan.rpc.common.enumeration.ResponseCode;
import com.hompan.rpc.common.message.CoapRequest;
import com.hompan.rpc.common.message.CoapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hompan.rpc.main.provider.ServiceProvider;
import com.hompan.rpc.main.provider.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 进行过程调用的处理器
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(CoapRequest CoapRequest) {
        Object service = serviceProvider.getServiceProvider(CoapRequest.getInterfaceName());
        return invokeTargetMethod(CoapRequest, service);
    }

    private Object invokeTargetMethod(CoapRequest CoapRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(CoapRequest.getMethodName(), CoapRequest.getParamTypes());
            result = method.invoke(service, CoapRequest.getParameters());
            logger.info("服务:{} 成功调用方法:{}", CoapRequest.getInterfaceName(), CoapRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return CoapResponse.fail(ResponseCode.METHOD_NOT_FOUND, CoapRequest.getRequestId());
        }
        return result;
    }

}