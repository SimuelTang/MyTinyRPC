package pers.simuel.rpc.serializer;

import pers.simuel.rpc.model.RpcRequest;

import java.util.UUID;

/**
 * @Author simuel_tang
 * @Date 2022/6/1
 * @Desc
 */
public class SerializableTest {

    // 设置测试次数
    static final int COUNT = 100000;
    
    public static void main(String[] args) {
        // 统计时间
        timeTest(new JDKSerializer(), "JDK");
        timeTest(new KryoSerializer(), "Kryo");
        timeTest(new HessianSerializer(), "Hessian");
        timeTest(new JSONSerializer(), "JSON");
        timeTest(new ProtoBufSerializer(), "ProtoBuff");
    }
    
    private static void timeTest(CommonSerializer serializer, String type) {
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName("test")
                .methodName("test")
                .paraTypes(new Class<?>[]{})
                .build();
        // 统计时间
        long serializeStartTime = System.currentTimeMillis();
        long deserializeStartTime = System.currentTimeMillis();
        long serializeEndTime = System.currentTimeMillis();
        long deserializeSEndTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            byte[] bytes = serializer.serialize(rpcRequest);
            serializeEndTime = System.currentTimeMillis();

            serializer.deserialize(RpcRequest.class, bytes);
            deserializeSEndTime = System.currentTimeMillis();
        }
        System.out.println(type + "序列化时间：" + (serializeEndTime - serializeStartTime) + "ms");
        System.out.println(type + "反序列化时间：" + (deserializeSEndTime - deserializeStartTime) + "ms");
        System.out.println("-------------");
    }
}
