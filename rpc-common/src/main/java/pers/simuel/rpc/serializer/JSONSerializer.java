package pers.simuel.rpc.serializer;

import com.alibaba.fastjson.JSON;
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

    @Override
    public int getSerializerType() {
        return JSON_TYPE;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] data) {
        return JSON.parseObject(new String(data),clazz);
    }

    @Override
    public <T> byte[] serialize(T object) {
        if (object  == null){
            throw new NullPointerException();
        }

        String json = JSON.toJSONString(object);
        return json.getBytes();
    }


}
