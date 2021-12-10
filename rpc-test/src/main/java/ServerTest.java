import pers.simuel.rpc.HelloServiceImpl;
import pers.simuel.rpc.registry.ServiceRegistry;
import pers.simuel.rpc.registry.impl.DefaultServiceRegistry;
import pers.simuel.rpc.server.RPCServer;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 12:42
 */
public class ServerTest {
    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(new HelloServiceImpl());
        RPCServer rpcServer = new RPCServer(9000, serviceRegistry);
        rpcServer.start();
    }
}
