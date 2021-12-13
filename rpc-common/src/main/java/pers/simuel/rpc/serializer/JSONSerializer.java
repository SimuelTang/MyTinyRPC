package pers.simuel.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.exceptions.SerializeException;

import java.io.IOException;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 10:10
 */
@Slf4j
public class JSONSerializer implements CommonSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int getSerializerType() {
        return JSON_TYPE;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error("反序列化失败", e);
            throw new SerializeException("反序列化失败", e);
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error("序列化失败", e);
            throw new SerializeException("序列化失败", e);
        }
    }


}
