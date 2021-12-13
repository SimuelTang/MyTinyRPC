package pers.simuel.rpc.serializer;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.exceptions.SerializeException;

import java.io.*;

/**
 * JDK的序列化方式就是使用Java官方提供的API在字节流对象间进行转化
 *
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 10:29
 */
@Slf4j
public class JDKSerializer implements CommonSerializer {
    @Override
    public int getSerializerType() {
        return JDK_TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("反序列化失败", e);
            throw new SerializeException("反序列化失败", e);
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            // 因为oos中传入了baos最为参数，所以oos写入object后可以自动转化为baos
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("序列化失败", e);
            throw new SerializeException("序列化失败", e);
        }
    }
}
