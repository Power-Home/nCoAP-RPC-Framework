package com.hompan.rpc.server;

import com.hompan.rpc.main.annotation.Service;
import com.hompan.rpc.services.HelloService;
import com.hompan.rpc.services.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 存在服务器的服务实现类
 */
@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String sayHi(Info clientInfo) {
        logger.info("接收到消息：{}" ,clientInfo.getMsg());
        return clientInfo.getMsg()+ " " +clientInfo.getName();
    }
}
