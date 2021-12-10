package pers.simuel.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 10:11
 */
@AllArgsConstructor
@Getter
public enum RPCError {
    // 服务相关异常
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("无法连接到注册中心"),
    // 协议相关异常
    UNKNOWN_PROTOCOL("无法识别的协议包"),
    UNKNOWN_PACKAGE_TYPE("无法识别的数据包"),
    // 序列化相关异常
    REGISTER_SERVICE_FAILED("注册时发生错误"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接失败");
    // 异常信息
    private final String message;
}

