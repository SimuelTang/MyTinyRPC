package pers.simuel.rpc.handler;

import com.alibaba.nacos.api.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.enums.ResponseStatus;
import pers.simuel.rpc.protocol.RPCRequest;
import pers.simuel.rpc.protocol.RPCResponse;

import java.lang.reflect.Method;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 13:15
 */
@Slf4j
public class RequestHandler {
    public Object handle(RPCRequest request, Object service) {
        Object result = null;
        try {
            result = invokeTargetMethod(request, service);
            log.info("客户端的服务：{} 成功调用方法 {}", request.getInterfaceName(), request.getMethodName());
        } catch (Exception e) {
            log.error("处理请求时发生错误", e);
        }
        return result;
    }

    private Object invokeTargetMethod(RPCRequest request, Object service) throws Exception {
        Method method;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParaTypes());
        } catch (NoSuchMethodException e) {
            return RPCResponse.failure(ResponseStatus.METHOD_NOT_FOUND);
        }
        return method.invoke(service, request.getParameters());
    }
}
