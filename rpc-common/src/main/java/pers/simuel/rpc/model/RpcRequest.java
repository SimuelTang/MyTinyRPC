package pers.simuel.rpc.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 发起请求的话，需要传递给注册中心一些信息，这样注册中心才知道调用的是哪个服务
 * 接口和方法名字是必须的，否则找不到对应的服务
 * 方法因为会重载，所以需要我们提供方法的所有参数和对应的类型
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 10:38
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    // 接口的名字
    private String interfaceName;
    // 方法的名字
    private String methodName;
    // 方法的参数
    private Object[] parameters;
    // 方法参数的类型
    private Class<?>[] paraTypes;
}
