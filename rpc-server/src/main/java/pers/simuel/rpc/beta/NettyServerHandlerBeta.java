package pers.simuel.rpc.beta;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.codec.Beat;
import pers.simuel.rpc.enums.RPCError;
import pers.simuel.rpc.enums.ResponseStatus;
import pers.simuel.rpc.handler.RequestHandler;
import pers.simuel.rpc.model.RpcRequest;
import pers.simuel.rpc.model.RpcResponse;
import pers.simuel.rpc.provider.ServiceProvider;
import pers.simuel.rpc.provider.impl.DefaultServiceProvider;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author simuel_tang
 * @Date 2022/06/01
 * @Desc
 */
@Slf4j
public class NettyServerHandlerBeta extends SimpleChannelInboundHandler<RpcRequest> {

    // 用于通过接口获取注册的服务
    private final Map<String, Object> handlerMap;
    private final ThreadPoolExecutor serverHandlerPool;

    public NettyServerHandlerBeta(Map<String, Object> handlerMap, final ThreadPoolExecutor threadPoolExecutor) {
        this.handlerMap = handlerMap;
        this.serverHandlerPool = threadPoolExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) {
        // 过滤掉ping信息
        if (Beat.BEAT_ID.equalsIgnoreCase(request.getRequestId())) {
            log.info("服务端接收到ping");
            return;
        }
        
        // 处理调用端的请求
        serverHandlerPool.execute(() -> {
            log.info("服务端收到请求:{}", request);
            // 每个响应对应的请求ID相同
            RpcResponse<?> response = new RpcResponse<>();
            response.setRequestId(request.getRequestId());
            try {
                Object result = handle(request);
                response = RpcResponse.success(result);
            } catch (Throwable t) {
                log.info("服务端处理请求失败");
                response = RpcResponse.failure(ResponseStatus.Failure);
            }
            // 通过Netty将响应发送出去   
            ctx.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture ->
                    log.info("服务端已为请求:{}回执响应消息", request.getRequestId()));
        });

    }

    private Object handle(RpcRequest request) throws Throwable {
        String interfaceName = request.getInterfaceName();
        String version = request.getVersion();
        Object service = this.handlerMap.get(interfaceName);
        if (service == null) {
            log.error("无法为接口{}找到对应的实现和版本{}", interfaceName, version);
            return null;
        }
        Class<?> serviceClass = service.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParaTypes();
        Object[] parameters = request.getParameters();
        // JDK动态代理
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(service, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
            log.warn("Channel idle in last {} seconds, close it", Beat.BEAT_TIMEOUT);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
