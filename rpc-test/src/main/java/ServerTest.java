import pers.simuel.rpc.HelloServiceImpl;
import pers.simuel.rpc.server.RPCServer;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 12:42
 */
public class ServerTest {
    public static void main(String[] args) {
        RPCServer rpcServer = new RPCServer();
        rpcServer.register(new HelloServiceImpl(), 9000);
    }
}
