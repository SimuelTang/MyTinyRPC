package pers.simuel.trpc.registry;

import java.net.InetSocketAddress;

/**
 * @Author simuel_tang
 * @Date 2021/10/31
 * @Time 22:15
 */
public interface ServiceDiscovery {
    InetSocketAddress lookupService(String serviceName);
}
