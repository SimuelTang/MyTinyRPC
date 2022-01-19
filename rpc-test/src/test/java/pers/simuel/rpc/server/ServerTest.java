package pers.simuel.rpc.server;

import pers.simuel.rpc.HelloServiceImpl;
import pers.simuel.rpc.provider.ServiceProvider;
import pers.simuel.rpc.provider.impl.DefaultServiceProvider;
import pers.simuel.rpc.server.impl.SocketServer;

/**
 * @Author simuel_tang
 * @Date 2021/12/13
 * @Time 9:10
 */
public class ServerTest {
    public static void main(String[] args) {
        ServiceProvider serviceRegistry = new DefaultServiceProvider();
        serviceRegistry.addServiceProvider(new HelloServiceImpl());
        SocketServer socketServer = new SocketServer(9000, serviceRegistry);
        socketServer.start();
    }
}
