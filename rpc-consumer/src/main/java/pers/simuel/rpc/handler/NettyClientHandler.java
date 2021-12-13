package pers.simuel.rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.protocol.RPCResponse;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 14:34
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse<?>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse response) throws Exception {
        log.info("客户端收到来自服务端的响应:{}", response);
        AttributeKey<RPCResponse<?>> key = AttributeKey.valueOf("rpcResponse");
        ctx.channel().attr(key).set(response);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        log.error("过程调用时有错误发生", e.getCause());
        ctx.close();
    }
}
