package pers.simuel.trpc.serializers;

/**
 * @Author simuel_tang
 * @Date 2021/11/5
 * @Time 16:24
 */
public class ProtobufSerializer implements CommonSerializer {
    @Override
    public int getSerializeType() {
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
