package pers.simuel.trpc.serializers;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 22:17
 */
public class JSONSerializer implements CommonSerializer {
    @Override
    public int getSerializeType() {
        return 0;
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
