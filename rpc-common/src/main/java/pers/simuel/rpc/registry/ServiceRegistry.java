package pers.simuel.rpc.registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    // 注册某个功能（服务）
    <T> void register(String serviceName, InetSocketAddress inetSocketAddress);
    // 获取服务提供者的地址
    InetSocketAddress lookupService(String serviceName);
}
