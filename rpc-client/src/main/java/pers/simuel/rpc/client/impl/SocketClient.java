package pers.simuel.rpc.client.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.client.RPCClient;
import pers.simuel.rpc.model.RpcRequest;

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
@Data
@AllArgsConstructor
public class SocketClient implements RPCClient {

    private final String host;
    private final int port;
    
    /**
     * 客户端发送请求
     *
     * @param request 发送的请求及数据
     * @return
     */
    public Object sendRequest(RpcRequest request) {
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
