package pers.simuel.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 12:19
 */
@Slf4j
public class SerializerTest {
    Student s = new Student("saber", 18);
    @Test
    public void JDKTest() throws IOException, ClassNotFoundException {
        // 序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(s);
        byte[] bytes = baos.toByteArray();
        // 反序列化
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Student student = (Student) ois.readObject();
        System.in.read();
    }
    
    @Test
    public void JSONTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 序列化
        byte[] bytes = objectMapper.writeValueAsBytes(s);
        String sVal = objectMapper.writeValueAsString(s);
        // 反序列化
        Class<?> clazz = Student.class;
        Object obj = objectMapper.readValue(bytes, clazz);
        Student student = (Student) obj;
        
        System.in.read();
    }
}
