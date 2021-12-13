package pers.simuel.rpc.client;

import pers.simuel.rpc.RPCClientProxy;
import pers.simuel.rpc.service.HelloObject;
import pers.simuel.rpc.service.HelloService;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 15:05
 */
public class NettyClientTest {
    public static void main(String[] args) {
        RPCClient nettyClient = new NettyClient("localhost", 9000);
        RPCClientProxy clientProxy = new RPCClientProxy(nettyClient);
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String ret = helloService.sayHello(new HelloObject(1001, "saber"));
        System.out.println(ret);
    }
}
