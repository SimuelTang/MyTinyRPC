package pers.simuel.trpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.common.util.NacosUtil;
import pers.simuel.trpc.loadblancer.LoadBalancer;
import pers.simuel.trpc.loadblancer.RandomLoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2021/10/31
 * @Time 20:32
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {
    
    private final LoadBalancer loadBalancer;
    
    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        if (loadBalancer == null) this.loadBalancer = new RandomLoadBalancer();
        else this.loadBalancer = loadBalancer;
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstances(serviceName);
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时发生错误");
        }
        return null;
    }
}
