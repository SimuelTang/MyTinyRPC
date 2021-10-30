package pers.simuel.trpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.codec.CommonDecoder;
import pers.simuel.trpc.codec.CommonEncoder;
import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.entity.RPCResponse;
import pers.simuel.trpc.exceptions.RPCError;
import pers.simuel.trpc.exceptions.RPCException;
import pers.simuel.trpc.registry.NacosServiceRegistry;
import pers.simuel.trpc.registry.ServiceRegistry;
import pers.simuel.trpc.serializers.CommonSerializer;
import pers.simuel.trpc.serializers.JDKSerializer;

import java.net.InetSocketAddress;

/**
 * @Author simuel_tang
 * @Date 2021/10/8
 * @Time 14:25
 */
@Slf4j
public class NettyClient implements RPCClient {


    private static final Bootstrap bootstrap;
    private CommonSerializer serializer;
    private final ServiceRegistry serviceRegistry;

    public NettyClient() {
        this.serviceRegistry = new NacosServiceRegistry();
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new JDKSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RPCRequest rpcRequest) {
        if(serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCError.SERIALIZER_NOT_FOUND);
        }
        try {
            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, new JDKSerializer());
//            ChannelFuture future = bootstrap.connect(host, port).sync();
//            log.info("客户端连接到服务器:{}, {}", host, port);
//            Channel channel = future.channel();
            if (channel.isActive()) {
                channel.writeAndFlush(rpcRequest)
                        .addListener(future1 -> {
                            if (future1.isSuccess()) {
                                log.info("客户端发送消息:{}", rpcRequest.toString());
                            } else {
                                log.error("客户端发送消息失败: {}", future1.cause().toString());
                            }
                        });
                channel.closeFuture().sync();
                AttributeKey<RPCResponse> key = AttributeKey.valueOf("rpcResponse");
                RPCResponse response = channel.attr(key).get();
                return response;
            }
        } catch (InterruptedException e) {
            log.error("发送消息时发生错误", e);
        }
        return null;
    }
    
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
    
    
}
