package pers.simuel.trpc.provider;

/**
 * @Author simuel_tang
 * @Date 2021/10/29
 * @Time 16:20
 * 用于保存和提供服务实例对象
 */
public interface ServiceProvider {
    <T> void addServiceProvider(T service);

    Object getServiceProvider(String serviceName);
}
