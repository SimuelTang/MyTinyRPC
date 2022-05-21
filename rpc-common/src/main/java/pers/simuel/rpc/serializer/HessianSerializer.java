package pers.simuel.rpc.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.exceptions.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 12:16
 */
@Slf4j
public class HessianSerializer implements CommonSerializer {
    @Override
    public int getSerializerType() {
        return HESSIAN_TYPE;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try {
            ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            Hessian2Input input = new Hessian2Input(bai);
            return (T)input.readObject(clazz);
        } catch (IOException e) {
            log.error("Hessian反序列化错误", e);
            throw new SerializeException("反序列化异常");
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        Hessian2Output ho = null;
        ByteArrayOutputStream bao;
        try {
            bao = new ByteArrayOutputStream();
            ho = new Hessian2Output(bao);
            ho.writeObject(object);
            ho.flush();
            return bao.toByteArray();
        } catch (Exception e) {
            log.error("Hessian序列化错误", e);
            throw new SerializeException("序列化异常");
        } finally {
            if (null != ho) {
                try {
                    ho.close();
                } catch (IOException e) {
                    log.error("Hessian流关闭异常", e);
                }
            }
        }
    }
}
