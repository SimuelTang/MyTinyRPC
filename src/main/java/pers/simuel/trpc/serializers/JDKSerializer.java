package pers.simuel.trpc.serializers;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.exceptions.SerializeException;

import java.io.*;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 21:53
 */
@Slf4j
public class JDKSerializer implements CommonSerializer {
    @Override
    public int getSerializeType() {
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
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("序列化失败", e);
            throw new SerializeException("序列化失败", e);
        }
    }
}
