package pers.simuel.trpc.client;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.entity.RPCRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 18:44
 */
@Slf4j
public class RPCClient {
    public Object sendRequest(RPCRequest request, String host, int port) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            oos.writeObject(request);
            oos.flush();
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("客户端发送请求失败", e);
            return null;
        }
    }
}
