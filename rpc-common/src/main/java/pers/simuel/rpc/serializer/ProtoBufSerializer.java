package pers.simuel.rpc.serializer;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 10:29
 */
public class ProtoBufSerializer implements CommonSerializer {
    @Override
    public int getSerializerType() {
        return PROTOBUF_TYPE;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return null;
    }

    @Override
    public <T> byte[] serialize(T object) {
        return new byte[0];
    }
}
