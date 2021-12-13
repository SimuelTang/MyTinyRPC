package pers.simuel.rpc.serializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 12:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {
    private String name;
    private int age;
}
