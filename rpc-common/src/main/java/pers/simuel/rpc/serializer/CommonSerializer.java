package pers.simuel.rpc.serializer;

public interface CommonSerializer {

    int JDK_TYPE = 0;
    int JSON_TYPE = 1;
    int PROTOBUF_TYPE = 2;
    int HESSIAN_TYPE = 3;

    int getSerializerType();

    // 反序列化方法
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    // 序列化方法
    <T> byte[] serialize(T object);

    static CommonSerializer getByType(int type) {
        switch (type) {
            case JSON_TYPE:
                return new JSONSerializer();
            case PROTOBUF_TYPE:
                return new ProtoBufSerializer();
            case HESSIAN_TYPE:
                return new HessianSerializer();
            case JDK_TYPE:
            default:
                return new JDKSerializer();
        }
    }

}
