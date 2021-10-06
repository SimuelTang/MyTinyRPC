package pers.simuel.trpc.serializers;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 21:20
 */
public interface CommonSerializer {
    int JDK_TYPE = 0;
    int JSON_TYPE = 1;

    // 字类根据自己的实现返回对应的序列化类型即可
    int getSerializeType();

    // 反序列化方法
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    // 序列化方法
    <T> byte[] serialize(T object);


}
