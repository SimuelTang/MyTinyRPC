package pers.simuel.rpc.server;

import pers.simuel.rpc.HelloServiceImpl;
import pers.simuel.rpc.registry.ServiceRegistry;
import pers.simuel.rpc.registry.impl.DefaultServiceRegistry;

/**
 * @Author simuel_tang
 * @Date 2021/12/13
 * @Time 9:10
 */
public class ServerTest {
    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(new HelloServiceImpl());
        SocketServer socketServer = new SocketServer(9000, serviceRegistry);
        socketServer.start();
    }
}
