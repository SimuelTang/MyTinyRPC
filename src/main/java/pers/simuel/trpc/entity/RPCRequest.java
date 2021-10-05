package pers.simuel.trpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 16:30
 */
@Builder
@Data
public class RPCRequest implements Serializable {
    // 接口的名字
    private String interfaceName;
    // 方法的名字
    private String methodName;
    // 方法的参数
    private Object[] parameters;
    // 方法参数的类型
    private Class<?>[] paraTypes;
}
