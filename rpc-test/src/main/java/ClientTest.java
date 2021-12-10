import pers.simuel.rpc.RPCClientProxy;
import pers.simuel.rpc.service.HelloObject;
import pers.simuel.rpc.service.HelloService;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 12:42
 */
public class ClientTest {
    public static void main(String[] args) {
        RPCClientProxy proxy = new RPCClientProxy("localhost", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        String ret = helloService.sayHello(new HelloObject(1, "saber"));
        System.out.println(ret);
    }
}
