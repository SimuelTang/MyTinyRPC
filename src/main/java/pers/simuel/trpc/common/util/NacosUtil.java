package pers.simuel.trpc.common.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.exceptions.RPCError;
import pers.simuel.trpc.exceptions.RPCException;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author simuel_tang
 * @Date 2021/10/31
 * @Time 20:36
 * 管理Nacos的连接
 */
@Slf4j
public class NacosUtil {
    // 负责注册和发现Nacos服务
    private static final NamingService NAMING_SERVICE;
    // 记录所有的服务名
    private static final Set<String> serviceNames;
    // 注销服务时需要的地址
    private static InetSocketAddress address;
    
    // Nacos地址
    private static final String SERVER_ADDR = "127.0.0.1:8848";
    
    static {
        NAMING_SERVICE = getNacosNamingService();
        serviceNames = new HashSet<>();
    }
    
    private static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接Nacos时发生错误", e);
            throw new RPCException(RPCError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }
    
    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        NAMING_SERVICE.registerInstance(serviceName, address.getHostName(), address.getPort());
        NacosUtil.address = address;
    }
    
    public static List<Instance> getAllInstances(String serviceName) throws NacosException {
        return NAMING_SERVICE.getAllInstances(serviceName);
    }
    
    public static void clearRegistry() {
        // 有服务且连接过注册中心时才能尝试注销
        if (!serviceNames.isEmpty() && address != null ) {
            // 获取注册中心的地址和端口号
            String hostName = address.getHostName();
            int port = address.getPort();
            // 获取已经注册了的服务
            for (String serviceName : serviceNames) {
                try {
                    NAMING_SERVICE.deregisterInstance(serviceName, hostName, port);
                } catch (NacosException e) {
                    log.error("注销服务{}失败", serviceName, e);
                }
            }
        }
    }
    
}
