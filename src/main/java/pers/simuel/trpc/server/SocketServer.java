package pers.simuel.trpc.server;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.entity.RPCResponse;
import pers.simuel.trpc.server.handler.RequestHandler;
import pers.simuel.trpc.server.registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 19:28
 */
@Slf4j
public class SocketServer implements RPCServer {
    private final static int CORE_POOL_SIZE = 5;
    private final static int MAXIMUM_POOL_SIZE = 50;
    private final static long KEEP_ALIVE_TIME = 60;
    private final static int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(ServiceRegistry serviceRegistry) {
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, workingQueue, threadFactory);
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * 服务端主线程只负责启动，注册功能交由另外的线程处理
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(24914)) {
            log.info("服务器正在启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("成功与客户端建立连接，Ip为：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }

  
    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        
    }

    static class WorkerThread implements Runnable {
        private final Socket socket;
        private final ServiceRegistry serviceRegistry;
        private final RequestHandler requestHandler;

        public WorkerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
            this.socket = socket;
            this.requestHandler = requestHandler;
            this.serviceRegistry = serviceRegistry;
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
