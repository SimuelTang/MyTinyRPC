package pers.simuel.rpc.server;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.handler.RequestHandler;
import pers.simuel.rpc.protocol.RPCRequest;
import pers.simuel.rpc.protocol.RPCResponse;
import pers.simuel.rpc.registry.ServiceRegistry;
import pers.simuel.rpc.registry.impl.DefaultServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 11:12
 */
@Slf4j
public class RPCServer {

    // 当前服务端启动时使用的端口号
    private final int port;

    // 配置线程池的静态变量信息
    private final static int CORE_POOL_SIZE = 5;
    private final static int MAXIMUM_POOL_SIZE = 50;
    private final static long KEEP_ALIVE_TIME = 60;
    private final static int BLOCKING_QUEUE_CAPACITY = 100;
    private final static ExecutorService threadPool;

    // 注册中心
    private final ServiceRegistry serviceRegistry;
    
    // 响应处理器(专门处理业务逻辑)
    private final RequestHandler requestHandler = new RequestHandler();

    static {
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        // 实例化线程池
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public RPCServer(int port) {
        this.port = port;
        this.serviceRegistry = new DefaultServiceRegistry();
    }

    public RPCServer(int port, ServiceRegistry serviceRegistry) {
        this.port = port;
        this.serviceRegistry = serviceRegistry;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器正在启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("成功与客户端建立连接，客户端Ip为：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket, serviceRegistry, requestHandler));
            }
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }
    
    private static class WorkerThread implements Runnable {

        private final Socket socket;
        private final ServiceRegistry serviceRegistry;
        private final RequestHandler requestHandler;

        public WorkerThread(Socket socket, ServiceRegistry registry, RequestHandler handler) {
            this.socket = socket;
            this.serviceRegistry = registry;
            this.requestHandler = handler;
            
        }

        @Override
        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                // 获取请求对象
                RPCRequest rpcRequest = (RPCRequest) ois.readObject();
                // 通过对象获取客户端需要调用的接口
                String interfaceName = rpcRequest.getInterfaceName();
                // 通过接口名字找到服务端对应的服务
                Object service = serviceRegistry.getService(interfaceName);
                // 交由处理器处理并获取结果
                Object rtnValue = requestHandler.handle(rpcRequest, service);
                // 将结果作为响应发送给客户端
                oos.writeObject(RPCResponse.success(rtnValue));
                oos.flush();
            } catch (Exception e) {
                log.error("服务端处理请求时出错", e);
            }
        }
    }
}
