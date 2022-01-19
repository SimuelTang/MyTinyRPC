package pers.simuel.rpc.provider;

public interface ServiceProvider {
    // 注册某个功能（服务）
    <T> void addServiceProvider(T service);
    // 通过名字获取服务端实现的功能
    Object getServiceProvider(String serviceName);
}
