package pers.simuel.trpc.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.entity.RPCResponse;
import pers.simuel.trpc.provider.ServiceProvider;
import pers.simuel.trpc.provider.ServiceProviderImpl;
import pers.simuel.trpc.server.handler.RequestHandler;
import pers.simuel.trpc.server.registry.DefaultServiceRegistry;
import pers.simuel.trpc.server.registry.ServiceRegistry;

/**
 * @Author simuel_tang
 * @Date 2021/10/8
 * @Time 14:35
 * 这个类主要用来处理来自客户端的request
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    
    private final RequestHandler requestHandler = new RequestHandler();
//    private final ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
    private final static ServiceProvider serviceProvider = new ServiceProviderImpl();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest request) throws Exception {
        try {
            log.info("服务端收到请求：{}", request);
            Object service = serviceProvider.getServiceProvider(request.getInterfaceName());
            Object result = requestHandler.handle(request, service);
            ChannelFuture future = ctx.writeAndFlush(RPCResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(request);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
