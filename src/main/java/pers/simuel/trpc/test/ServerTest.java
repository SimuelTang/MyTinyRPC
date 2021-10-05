package pers.simuel.trpc.test;

import pers.simuel.trpc.server.RPCServer;
import pers.simuel.trpc.server.SaleServiceImpl;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 20:29
 */
public class ServerTest {
    public static void main(String[] args) {
        final RPCServer rpcServer = new RPCServer();
        rpcServer.register(new SaleServiceImpl(), 9000);
    }
}
