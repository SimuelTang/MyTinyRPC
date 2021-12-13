package pers.simuel.rpc.handler;

import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.enums.ResponseStatus;
import pers.simuel.rpc.protocol.RPCRequest;
import pers.simuel.rpc.protocol.RPCResponse;
import pers.simuel.rpc.registry.ServiceRegistry;
import pers.simuel.rpc.registry.impl.DefaultServiceRegistry;

/**
 * 采用SimpleChannelInboundHandler是因为它会自动释放资源
 *
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 12:34
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    // 用于通过接口获取注册的服务
    private final ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
    // 请求处理器
    private final RequestHandler requestHandler = new RequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest request) throws Exception {
        log.info("服务端收到请求:{}", request);
        String interfaceName = request.getInterfaceName();
        Object service = serviceRegistry.getService(interfaceName);
        Object ret = requestHandler.handle(request, service);
        ChannelFuture future = ctx.writeAndFlush(RPCResponse.success(ret));
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }


}
