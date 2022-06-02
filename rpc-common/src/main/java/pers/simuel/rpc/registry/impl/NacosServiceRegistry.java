package pers.simuel.rpc.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.enums.RPCError;
import pers.simuel.rpc.exceptions.RPCException;
import pers.simuel.rpc.loadbalancer.RpcLoadBalance;
import pers.simuel.rpc.loadbalancer.impl.RoundRobinLoadBalance;
import pers.simuel.rpc.registry.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author simuel_tang
 * @Date 2022/1/18
 * @Time 10:38
 * @Desc 采用Nacos作为注册中心，供提供者注册和消费端查找
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    // 注册服务提供者提供的服务
    private NamingService namingService;
    // service信息
    private final List<String> serviceList = new ArrayList<>();
    // 负载均衡器
    private final RpcLoadBalance rpcLoadBalance = new RoundRobinLoadBalance();

    // 默认初始化
    {
        try {
            // nacos默认地址为本地
            String DEFAULT_SERVER_ADDR = "127.0.0.1:8848";
            namingService = NamingFactory.createNamingService(DEFAULT_SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RPCException(RPCError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public NacosServiceRegistry() {

    }

    public NacosServiceRegistry(String registryAddress) {
        try {
            namingService = NamingFactory.createNamingService(registryAddress);
        } catch (NacosException e) {
            log.error("连接Nacos服务器地址:{}时有错误发生: ", registryAddress, e);
            throw new RPCException(RPCError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public <T> void register(String serviceName, InetSocketAddress address) {
        try {
            namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        } catch (NacosException e) {
            log.error("注册服务时发生错误", e);
            throw new RPCException(RPCError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            Instance instance = rpcLoadBalance.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("查找服务时发生错误", e);
        }
        return null;
    }

    public void registerService(String host, int port, Map<String, Object> serviceMap) {
        for (String service : serviceMap.keySet()) {
            try {
                namingService.registerInstance(service, host, port);
                serviceList.add(service);
            } catch (NacosException e) {
                log.error("注册服务时发生错误", e);
                throw new RPCException(RPCError.REGISTER_SERVICE_FAILED);
            }
        }
    }

}
