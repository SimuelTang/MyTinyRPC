package pers.simuel.rpc.provider;

/**
 * Provider用于在本地存储服务提供者的信息
 */
public interface ServiceProvider {
    // 注册某个功能（服务）
    <T> void addServiceProvider(T service);
    // 通过名字获取服务端实现的功能
    Object getServiceProvider(String serviceName);
}
