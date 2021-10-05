package pers.simuel.trpc.test;

import pers.simuel.trpc.client.RPCClientProxy;
import pers.simuel.trpc.common.Product;
import pers.simuel.trpc.common.SaleService;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 20:30
 */
public class ClientTest {
    public static void main(String[] args) {
        final RPCClientProxy proxy = new RPCClientProxy("localhost", 9000);
        final SaleService saleService = proxy.getProxy(SaleService.class);
        final Product product = new Product("apple", 10);
        final String res = saleService.buy(product);
        System.out.println(res);
    }
}
