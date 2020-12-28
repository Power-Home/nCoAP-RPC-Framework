package com.hompan.rpc.main.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.hompan.rpc.common.enumeration.RpcError;
import com.hompan.rpc.common.exception.RpcException;
import com.hompan.rpc.main.serializer.KryoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Hom Pan
 * @date 2020/12/11 21:58
 */

public class HashLoadBalancer implements LoadBalancer {

    private static final Logger logger = LoggerFactory.getLogger(HashLoadBalancer.class);

    @Override
    public Instance select(List<Instance> instances) {
        return select(instances,null);
    }

    @Override
    public Instance select(List<Instance> instances,String IP) {
        if(IP==null || IP.length()<6) {
            logger.error("不合法的源IP地址");
            throw new RpcException(RpcError.ILLEGAL_IP_ADDRESS);
        }
        int hash = IP.hashCode();
        int size = instances.size();
        return instances.get(hash%size);
    }
}
