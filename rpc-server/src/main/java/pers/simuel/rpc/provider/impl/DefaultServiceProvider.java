package pers.simuel.rpc.provider.impl;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.enums.RPCError;
import pers.simuel.rpc.exceptions.RPCException;
import pers.simuel.rpc.provider.ServiceProvider;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 13:13
 */
@Slf4j
public class DefaultServiceProvider implements ServiceProvider {

    private final static Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final static Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T serviceProvider) {
        try {
            // 获取当前服务接口的全类名
            String serviceName = serviceProvider.getClass().getCanonicalName();
            // 如果已经注册过，直接返回
            if (registeredService.contains(serviceName)) {
                return;
            }
            // 添加当前服务
            registeredService.add(serviceName);
            // 获取当前服务实现的所有接口
            Class<?>[] interfaces = serviceProvider.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new RPCException(RPCError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
            }
            // 记录这些接口对应的服务，保证某个接口只能有一个对象提供服务
            for (Class<?> inter : interfaces) {
                serviceMap.put(inter.getCanonicalName(), serviceProvider);
            }
            log.info("向接口：{} 注册服务：{}", interfaces, serviceProvider);
        } catch (Exception e) {
            log.error("服务注册失败", e);
        }
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RPCException(RPCError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
