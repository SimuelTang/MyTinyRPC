package pers.simuel.rpc.handler;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.enums.ResponseStatus;
import pers.simuel.rpc.model.RpcRequest;
import pers.simuel.rpc.model.RpcResponse;

import java.lang.reflect.Method;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 13:15
 */
@Slf4j
public class RequestHandler {
    
    
    public Object handle(RpcRequest request, Object service) {
        Object result = null;
        try {
            result = invokeTargetMethod(request, service);
            log.info("客户端的服务：{} 成功调用方法 {}", request.getInterfaceName(), request.getMethodName());
        } catch (Exception e) {
            log.error("处理请求时发生错误", e);
        }
        return result;
    }

    private Object invokeTargetMethod(RpcRequest request, Object service) throws Exception {
        Method method;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParaTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.failure(ResponseStatus.METHOD_NOT_FOUND);
        }
        return method.invoke(service, request.getParameters());
    }
}
