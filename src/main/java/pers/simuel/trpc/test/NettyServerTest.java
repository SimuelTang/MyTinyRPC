package pers.simuel.trpc.test;

import pers.simuel.trpc.client.NettyClient;
import pers.simuel.trpc.client.RPCClientProxy;
import pers.simuel.trpc.common.Product;
import pers.simuel.trpc.common.SaleService;
import pers.simuel.trpc.server.NettyServer;
import pers.simuel.trpc.server.impl.SaleServiceImpl;
import pers.simuel.trpc.server.registry.DefaultServiceRegistry;

/**
 * @Author simuel_tang
 * @Date 2021/10/8
 * @Time 19:02
 */
public class NettyServerTest {
    public static void main(String[] args) {
        SaleServiceImpl saleService = new SaleServiceImpl();
        DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(saleService);
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(24914);
    }
}
