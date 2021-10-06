package pers.simuel.trpc.server.registry;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.exceptions.RPCError;
import pers.simuel.trpc.exceptions.RPCException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 12:44
 * @Description 默认的注册功能
 * 1. 我们需要保存待注册的服务，同时因为这些服务是唯一的，所以可以使用一个Set集合。
 * 2. 我们还要能够通过服务的名字找到具体的服务实现，所以可以使用一个哈希表结构。
 */

@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry {

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void registry(T service) {
        // 获取当前服务接口的全类名
        String serviceName = service.getClass().getCanonicalName();
        // 如果已经注册过，直接返回
        if (registeredService.contains(serviceName)) return;
        // 添加当前服务
        registeredService.add(serviceName);
        // 获取当前服务实现的所有接口
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RPCException(RPCError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        // 记录这些接口对应的服务，保证某个接口只能有一个对象提供服务
        for (Class<?> inter : interfaces) {
            serviceMap.put(inter.getCanonicalName(), service);
        }
        log.info("向接口：{} 注册服务：{}", interfaces, service);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RPCException(RPCError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
