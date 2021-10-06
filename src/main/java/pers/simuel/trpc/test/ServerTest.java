package pers.simuel.trpc.test;

import pers.simuel.trpc.server.RPCServer;
import pers.simuel.trpc.server.impl.SaleServiceImpl;
import pers.simuel.trpc.server.registry.DefaultServiceRegistry;
import pers.simuel.trpc.server.registry.ServiceRegistry;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 20:29
 */
public class ServerTest {
    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(new SaleServiceImpl());
        final RPCServer rpcServer = new RPCServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
