package pers.simuel.trpc.client;

import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.entity.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 18:50
 */
public class RPCClientProxy implements InvocationHandler {

    private final RPCClient rpcClient;

    public RPCClientProxy(RPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        // 构建request对象
        RPCRequest rpcRequest = RPCRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paraTypes(method.getParameterTypes())
                .build();
        // 发送request对象并获取结果
        return ((RPCResponse<?>) rpcClient.sendRequest(rpcRequest)).getData();
    }
}
