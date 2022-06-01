package pers.simuel.rpc.server;

import pers.simuel.rpc.annotations.RpcServiceScan;
import pers.simuel.rpc.core.NettyServer;
import pers.simuel.rpc.provider.ServiceProvider;
import pers.simuel.rpc.provider.impl.DefaultServiceProvider;
import pers.simuel.rpc.service.HelloService;
import pers.simuel.rpc.service.HelloServiceImpl;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 15:04
 */
@RpcServiceScan
public class NettyServerTest {
    public static void main(String[] args) {
        // 注册服务(保留在本地的信息)
        ServiceProvider serviceProvider = new DefaultServiceProvider();
        HelloService helloService = new HelloServiceImpl();
        serviceProvider.addServiceProvider(helloService);
        // 开启服务提供者
        NettyServer nettyServer = new NettyServer("localhost", 9000);
        nettyServer.publishService(helloService, HelloService.class);
        nettyServer.start();
    }
}
