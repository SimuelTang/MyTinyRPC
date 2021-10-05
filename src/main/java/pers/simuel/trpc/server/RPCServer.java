package pers.simuel.trpc.server;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.entity.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
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
public class RPCServer {
    private final ExecutorService threadPool;

    public RPCServer() {
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void register(Object service, int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器正在启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("成功与客户端建立连接，Ip为：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket, service));
            }
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }

    static class WorkerThread implements Runnable {
        private final Socket socket;
        private final Object service;

        public WorkerThread(Socket socket, Object service) {
            this.socket = socket;
            this.service = service;
        }

        @Override
        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                // 获取请求对象
                RPCRequest rpcRequest = (RPCRequest) ois.readObject();
                // 获取待调用的方法
                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParaTypes());
                // 通过反射调用方法
                Object rtnValue = method.invoke(service, rpcRequest.getParameters());
                // 将结果作为响应发送给客户端
                oos.writeObject(RPCResponse.success(rtnValue));
                oos.flush();
            } catch (Exception e) {
                log.error("服务端处理请求时出错", e);
            }
        }
    }
}
