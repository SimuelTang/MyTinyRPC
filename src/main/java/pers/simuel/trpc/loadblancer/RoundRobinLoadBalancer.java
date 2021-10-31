package pers.simuel.trpc.loadblancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2021/10/31
 * @Time 21:55
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    
    private int index = 0;
    
    @Override
    public Instance select(List<Instance> instances) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }
}
