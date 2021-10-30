package pers.simuel.trpc.registry;

import java.net.InetSocketAddress;

/**
 * @Author simuel_tang
 * @Date 2021/10/29
 * @Time 10:52
 */
public interface ServiceRegistry {
    // 供提供者注册服务
    void register(String serviceName, InetSocketAddress inetSocketAddress);
    // 供消费者查找服务
    InetSocketAddress lookupService(String serviceName);
}
