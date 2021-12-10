package pers.simuel.rpc;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.protocol.RPCRequest;
import pers.simuel.rpc.protocol.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 10:57
 */
@Slf4j
public class RPCClientProxy implements InvocationHandler {

    private final String host;
    private final Integer port;

    public RPCClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        // 传入类加载器、接口、handler就可以获取对应的实例
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
        // 发送request对象，强转为response对象后获取结果
        RPCClient rpcClient = new RPCClient();
        return ((RPCResponse<?>) rpcClient.sendRequest(rpcRequest, host, port)).getData();
    }
}
