package pers.simuel.trpc.provider;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.exceptions.RPCError;
import pers.simuel.trpc.exceptions.RPCException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author simuel_tang
 * @Date 2021/10/29
 * @Time 16:20
 * 用于保存和提供服务实例对象
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
    
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public <T> void addServiceProvider(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RPCException(RPCError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        log.info("向接口:{}注册服务:{}", interfaces, serviceName);
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
