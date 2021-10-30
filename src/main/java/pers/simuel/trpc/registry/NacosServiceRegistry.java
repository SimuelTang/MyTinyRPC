package pers.simuel.trpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.exceptions.RPCError;
import pers.simuel.trpc.exceptions.RPCException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2021/10/29
 * @Time 10:53
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    // 注册中心的地址（暂用本地）
    private static final String SERVER_ADDR = "127.0.0.1:8848";
    private static final NamingService NAMING_SERVICE;

    static {
        try {
            NAMING_SERVICE = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生", e);
            throw new RPCException(RPCError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NAMING_SERVICE.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            log.error("注册服务时有错误发生:", e);
            throw new RPCException(RPCError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            // 获取注册中心中已经注册了的服务
            List<Instance> instances = NAMING_SERVICE.getAllInstances(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
