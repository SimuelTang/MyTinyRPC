package pers.simuel.rpc.serializer;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 12:16
 */
public class HessianSerializer implements CommonSerializer {
    @Override
    public int getSerializerType() {
        return HESSIAN_TYPE;
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
