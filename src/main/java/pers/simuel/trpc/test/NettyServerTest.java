package pers.simuel.trpc.test;

import pers.simuel.trpc.common.SaleService;
import pers.simuel.trpc.serializers.JDKSerializer;
import pers.simuel.trpc.server.NettyServer;
import pers.simuel.trpc.server.impl.SaleServiceImpl;

/**
 * @Author simuel_tang
 * @Date 2021/10/8
 * @Time 19:02
 */
public class NettyServerTest {
    public static void main(String[] args) {
        SaleService saleService = new SaleServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1", 62000);
        nettyServer.setSerializer(new JDKSerializer());
        nettyServer.publishService(saleService, SaleService.class);
    }
    
}
