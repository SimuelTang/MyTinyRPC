package pers.simuel.rpc.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.simuel.rpc.enums.RPCError;
import pers.simuel.rpc.exceptions.RPCException;
import pers.simuel.rpc.registry.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2022/1/18
 * @Time 10:38
 * @Desc 采用Nacos作为注册中心，供提供者注册和消费端查找
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    // nacos地址
    private static final String SERVER_ADDR = "127.0.0.1:8848";
    // 注册服务提供者提供的服务
    private static final NamingService namingService;

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
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
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("查找服务时发生错误", e);
        }
        return null;
    }
}
