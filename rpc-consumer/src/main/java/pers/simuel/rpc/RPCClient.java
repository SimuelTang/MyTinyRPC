package pers.simuel.rpc;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.protocol.RPCRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 11:03
 */
@Slf4j
public class RPCClient {
    /**
     * 客户端发送请求
     *
     * @param request 发送的请求及数据
     * @param host    一般为注册中心的地址
     * @param port    对应的端口号
     * @return
     */
    public Object sendRequest(RPCRequest request, String host, int port) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            // 将请求写到输出流发送出去，如果有对象需要接收则最后返回时调用read方法
            oos.writeObject(request);
            oos.flush();
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("客户端发送请求失败", e);
            return null;
        }
    }
}
