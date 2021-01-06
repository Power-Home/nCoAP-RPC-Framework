package com.hompan.rpc.main.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hompan.rpc.main.annotation.Service;
import com.hompan.rpc.main.annotation.ServiceScan ;
import com.hompan.rpc.common.enumeration.RpcError;
import com.hompan.rpc.common.exception.RpcException;
import com.hompan.rpc.main.provider.ServiceProvider;
import com.hompan.rpc.main.registry.ServiceRegistry;
import com.hompan.rpc.common.util.ReflectUtil;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author PanHom
 * nettyserver 和 socketserver的父类，通过扫描注解实现了父接口的publishService方法
 *
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    public void scanServices() {
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(ServiceScan.class)) {
                logger.error("启动类缺少 @ServiceScan 注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            logger.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for(Class<?> clazz : classSet) {
            if(clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                if("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface: interfaces){
                        publishService(obj, oneInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }
            }
        }
    }

    @Override
    public <T> void publishService(T service, String serviceName) {
        //将反射创建的服务实现类及其名字放入服务器中的map中
        serviceProvider.addServiceProvider(service, serviceName);
        //将扫描到的servicename，比如sayHi，注册进nacos注册中心
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }

}
