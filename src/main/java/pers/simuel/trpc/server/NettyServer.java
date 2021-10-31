package pers.simuel.trpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.codec.CommonDecoder;
import pers.simuel.trpc.codec.CommonEncoder;
import pers.simuel.trpc.exceptions.RPCError;
import pers.simuel.trpc.exceptions.RPCException;
import pers.simuel.trpc.hook.ShutdownHook;
import pers.simuel.trpc.provider.ServiceProvider;
import pers.simuel.trpc.provider.ServiceProviderImpl;
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
public class NettyServer implements RPCServer {

    private final String host;
    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    private CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new CommonEncoder(new JDKSerializer()));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            // 启动时添加钩子
            ShutdownHook.getShutdownHook().addClearAllHook();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("服务端启动发生错误", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RPCException(RPCError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
