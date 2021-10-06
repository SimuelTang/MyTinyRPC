package pers.simuel.trpc.server.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RPCError {
    // RPC中常见异常
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口");

    // 异常信息
    private final String message;
}
