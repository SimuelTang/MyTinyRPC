package pers.simuel.trpc.server.registry;

/**
 * 规定基本的服务注册功能
 */
public interface ServiceRegistry {
    <T> void registry(T service);

    Object getService(String serviceName);
}
