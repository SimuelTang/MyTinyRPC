package pers.simuel.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.codec.CommonDecoder;
import pers.simuel.rpc.codec.CommonEncoder;
import pers.simuel.rpc.handler.NettyClientHandler;
import pers.simuel.rpc.protocol.RPCRequest;
import pers.simuel.rpc.protocol.RPCResponse;
import pers.simuel.rpc.serializer.JDKSerializer;

/**
 * 通过Netty与作为通信方式的消费端
 *
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 14:38
 */
@Slf4j
public class NettyClient implements RPCClient {
    
    private final String host;
    private final int port;

    private static final Bootstrap bootstrap;

    static {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() { // 本身就一个连接，所以没有childHandler
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        log.info("建立连接，准备初始化部分信息");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new JDKSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object sendRequest(RPCRequest request) {
        try {
            // 与对端建立连接
            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务器:{} {}", host, port);
            // 尝试向服务端发送请求并读取响应
            Channel channel = future.channel();
            channel.writeAndFlush(request).addListener(f -> {
                if (f.isSuccess()) {
                    log.info("客户端成功发送消息{}", request.toString());
                } else {
                    log.error("客户端发送消息时有错误发生", f.cause());
                }
            });
            channel.closeFuture().sync(); // 阻塞
            AttributeKey<RPCResponse<?>> key = AttributeKey.valueOf("rpcResponse");
            return channel.attr(key).get(); // 返回服务端响应的RPCResponse
        } catch (InterruptedException e) {
            log.error("发消息时有错误发生", e);
        }
        return null;
    }
}
