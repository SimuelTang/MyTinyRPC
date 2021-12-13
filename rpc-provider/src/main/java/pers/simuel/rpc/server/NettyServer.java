package pers.simuel.rpc.server;

import com.fasterxml.jackson.databind.JsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.codec.CommonDecoder;
import pers.simuel.rpc.codec.CommonEncoder;
import pers.simuel.rpc.handler.NettyServerHandler;
import pers.simuel.rpc.registry.ServiceRegistry;
import pers.simuel.rpc.registry.impl.DefaultServiceRegistry;
import pers.simuel.rpc.serializer.JDKSerializer;
import pers.simuel.rpc.serializer.JSONSerializer;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 15:14
 */
@Slf4j
public class NettyServer implements RPCServer {

    private final int port;
    
    
    public NettyServer(int port) {
        this.port = port;
    }
    
    @Override
    public void start() {
        // Netty基于多路复用机制，分为两类线程
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 启动服务器并添加配置
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)          // 表示这是一个服务端的线程
                    .handler(new LoggingHandler(LogLevel.INFO))     // 为服务端流程设置日志
                    .option(ChannelOption.SO_BACKLOG, 256)          // 指定服务端连接队列大小
                    .option(ChannelOption.SO_KEEPALIVE, true)       // 检测对端是否处于连接状态
                    .childOption(ChannelOption.TCP_NODELAY, true)   // 开启Nagle算法
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 与建立连接的Socket通信时使用的Handler
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(new JDKSerializer()));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // 通过ChannelFuture保证IO操作完成后才关闭
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
