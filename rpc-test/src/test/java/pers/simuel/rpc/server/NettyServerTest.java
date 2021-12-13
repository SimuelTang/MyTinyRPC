package pers.simuel.rpc.server;

import pers.simuel.rpc.HelloServiceImpl;
import pers.simuel.rpc.registry.ServiceRegistry;
import pers.simuel.rpc.registry.impl.DefaultServiceRegistry;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 15:04
 */
public class NettyServerTest {
    public static void main(String[] args) {
        // 注册服务
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(new HelloServiceImpl());
        // 开启服务提供者
        NettyServer nettyServer = new NettyServer(9000);
        nettyServer.start();
    }
}
