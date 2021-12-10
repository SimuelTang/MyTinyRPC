package pers.simuel.rpc.registry;

public interface ServiceRegistry {
    // 注册某个功能（服务）
    <T> void registry(T service);
    // 通过名字获取服务端实现的功能
    Object getService(String serviceName);
}
