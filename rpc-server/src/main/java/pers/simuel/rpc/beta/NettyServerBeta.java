package pers.simuel.rpc.beta;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.core.NettyServer;
import pers.simuel.rpc.core.RpcServerInitializer;
import pers.simuel.rpc.provider.ServiceProvider;
import pers.simuel.rpc.provider.impl.DefaultServiceProvider;
import pers.simuel.rpc.registry.impl.NacosServiceRegistry;
import pers.simuel.rpc.utils.ThreadPoolUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 15:14
 */
@Slf4j
public class NettyServerBeta{

    private Thread thread;
    private final String serverAddress;
    private final NacosServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    private final Map<String, Object> serviceMap = new HashMap<>();

    
    private final Integer CORE_POOL_SIZE = 16;
    private final Integer MAX_POOL_SIZE = 32;
    
    public NettyServerBeta(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = new NacosServiceRegistry(registryAddress);
        this.serviceProvider = new DefaultServiceProvider();
    }

    public void addService(String interfaceName, String version, Object serviceBean) {
        log.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
        serviceMap.put(interfaceName, serviceBean);
        serviceProvider.addServiceProvider(serviceBean);
    }

    public void start() {
        thread = new Thread(new Runnable() {
            final ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.makeServerThreadPool(
                    NettyServer.class.getSimpleName(), CORE_POOL_SIZE, MAX_POOL_SIZE);

            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new RpcServerInitializer(serviceMap, threadPoolExecutor))
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);

                    String[] array = serverAddress.split(":");
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    ChannelFuture future = bootstrap.bind(host, port).sync();
                    serviceRegistry.registerService(host, port, serviceMap);
                    log.info("服务端已启动，所使用的端口为:{}", port);
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        log.info("服务端已停止");
                    } else {
                        log.error("服务端发生错误", e);
                    }
                } finally {
                    try {
                        workerGroup.shutdownGracefully();
                        bossGroup.shutdownGracefully();
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }
            }
        });
        thread.start();
    }

    public void stop() {
        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}
