package pers.simuel.rpc.server;

import pers.simuel.rpc.HelloServiceImpl;
import pers.simuel.rpc.provider.ServiceProvider;
import pers.simuel.rpc.provider.impl.DefaultServiceProvider;
import pers.simuel.rpc.server.impl.NettyServer;
import pers.simuel.rpc.service.HelloService;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 15:04
 */
public class NettyServerTest {
    public static void main(String[] args) {
        // 注册服务(保留在本地的信息)
        ServiceProvider serviceRegistry = new DefaultServiceProvider();
        HelloService helloService = new HelloServiceImpl();
        serviceRegistry.addServiceProvider(helloService);
        // 开启服务提供者
        NettyServer nettyServer = new NettyServer("localhost", 9000);
        nettyServer.publishService(helloService, HelloService.class);
        nettyServer.start();
    }
}
