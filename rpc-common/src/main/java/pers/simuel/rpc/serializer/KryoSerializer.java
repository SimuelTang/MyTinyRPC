package pers.simuel.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.exceptions.SerializeException;
import pers.simuel.rpc.model.RpcRequest;
import pers.simuel.rpc.model.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 10:29
 */
@Slf4j
public class KryoSerializer implements CommonSerializer {
    
//    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
//        Kryo kryo = new Kryo();
//        kryo.register(RpcRequest.class);
//        kryo.register(RpcResponse.class);
//        kryo.setReferences(true);
//        kryo.setRegistrationRequired(false);
//        return kryo;
//    });
    
    private static final Kryo kryo;
    
    static {
        kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
    }
    
    @Override
    public int getSerializerType() {
        return KRYO_TYPE;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try(ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            Input input = new Input(bai)) {
//            Kryo kryo = KRYO_THREAD_LOCAL.get();
            T object = kryo.readObject(input, clazz);
//            KRYO_THREAD_LOCAL.remove();
            return object;
        } catch (Exception e) {
            log.error("Kryo反序列化出错", e);
            throw new SerializeException("反序列化异常");
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        try(ByteArrayOutputStream bao = new ByteArrayOutputStream();
            Output output = new Output(bao)) {
//            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(output, object);
//            KRYO_THREAD_LOCAL.remove();
            return output.toBytes();
        } catch (Exception e) {
            log.error("Kryo序列化错误", e);
            throw new SerializeException("序列化异常");
        }
    }
}
