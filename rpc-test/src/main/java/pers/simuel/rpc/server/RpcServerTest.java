package pers.simuel.rpc.server;

import pers.simuel.rpc.annotations.RpcServiceScan;
import pers.simuel.rpc.beta.NettyServerBeta;
import pers.simuel.rpc.core.NettyServer;
import pers.simuel.rpc.core.RPCServer;
import pers.simuel.rpc.service.HelloService;
import pers.simuel.rpc.service.HelloServiceImpl;

/**
 * @Author simuel_tang
 * @Date 2022/6/1
 * @Desc
 */
@RpcServiceScan
public class RpcServerTest {
    public static void main(String[] args) {
        String serverAddr = "127.0.0.1:24914";
        String registryAddr = "127.0.0.1:8848";
        NettyServerBeta server = new NettyServerBeta(serverAddr, registryAddr);

        HelloService helloService = new HelloServiceImpl();
        server.addService(HelloService.class.getName(), "1.0", helloService);
        
        server.start();
    }
}
