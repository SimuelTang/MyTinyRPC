package pers.simuel.trpc.test;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import pers.simuel.trpc.client.NettyClient;
import pers.simuel.trpc.client.RPCClientProxy;
import pers.simuel.trpc.common.Product;
import pers.simuel.trpc.common.SaleService;
import pers.simuel.trpc.serializers.JDKSerializer;

/**
 * @Author simuel_tang
 * @Date 2021/10/8
 * @Time 19:02
 */
public class NettyClientTest {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        client.setSerializer(new JDKSerializer());
        RPCClientProxy proxy = new RPCClientProxy(client);
        SaleService saleService = proxy.getProxy(SaleService.class);
        String ret = saleService.buy(new Product("apple", 5));
        System.out.println(ret);
    }
}
