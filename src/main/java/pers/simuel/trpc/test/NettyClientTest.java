package pers.simuel.trpc.test;

import pers.simuel.trpc.client.NettyClient;
import pers.simuel.trpc.client.RPCClientProxy;
import pers.simuel.trpc.common.Product;
import pers.simuel.trpc.common.SaleService;

/**
 * @Author simuel_tang
 * @Date 2021/10/8
 * @Time 19:02
 */
public class NettyClientTest {
    public static void main(String[] args) {
        NettyClient client = new NettyClient("localhost", 24914);
        RPCClientProxy proxy = new RPCClientProxy(client);
        SaleService saleService = proxy.getProxy(SaleService.class);
        String ret = saleService.buy(new Product("apple", 5));
        System.out.println(ret);
    }
}
