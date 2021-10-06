package pers.simuel.trpc.server.handler;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.entity.RPCResponse;
import pers.simuel.trpc.enumeration.ResponseCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 15:32
 * <p>
 * 这个类主要是用来处理来自客户端的请求，所以需要配合本地服务一起使用
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

    private Object invokeTargetMethod(RPCRequest request, Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParaTypes());
        } catch (NoSuchMethodException e) {
            return RPCResponse.failure(ResponseCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service, request.getParameters());
    }
}
