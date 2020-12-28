package com.hompan.rpc.main.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public Instance select(List<Instance> instances) {
        return select(instances, null);
    }

    @Override
    public Instance select(List<Instance> instances, String IP) {
        return instances.get(new Random().nextInt(instances.size()));
    }
}
