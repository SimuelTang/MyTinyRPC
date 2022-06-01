package pers.simuel.rpc.client;

import pers.simuel.rpc.RPCClientProxy;
import pers.simuel.rpc.client.impl.NettyClient;
import pers.simuel.rpc.service.HelloService;

/**
 * @Author simuel_tang
 * @Date 2022/6/1
 * @Desc
 */
public class RpcClientTest {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        RPCClientProxy clientProxy = new RPCClientProxy(nettyClient);
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String ret = helloService.hello("saber");
        System.out.println(ret);
    }
}
