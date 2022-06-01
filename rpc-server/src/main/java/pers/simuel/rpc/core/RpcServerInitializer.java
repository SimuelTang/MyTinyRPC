package pers.simuel.rpc.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.beta.NettyServerHandlerBeta;
import pers.simuel.rpc.codec.CommonDecoder;
import pers.simuel.rpc.codec.CommonEncoder;
import pers.simuel.rpc.handler.NettyServerHandler;
import pers.simuel.rpc.model.RpcRequest;
import pers.simuel.rpc.model.RpcResponse;
import pers.simuel.rpc.serializer.CommonSerializer;
import pers.simuel.rpc.serializer.KryoSerializer;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author simuel_tang
 * @Date 2022/5/31
 * @Desc 服务端初始化信息
 */
@Slf4j
@NoArgsConstructor
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {
    private Map<String, Object> handlerMap;
    private ThreadPoolExecutor threadPoolExecutor;

    public RpcServerInitializer(Map<String, Object> handlerMap, ThreadPoolExecutor threadPoolExecutor) {
        this.handlerMap = handlerMap;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        log.info("服务端成功启动，开始进行初始化操作");
        CommonSerializer serializer = KryoSerializer.class.newInstance();
        ChannelPipeline cp = channel.pipeline();
        cp.addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
//        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new CommonDecoder());
        cp.addLast(new CommonEncoder(serializer));
        cp.addLast(new NettyServerHandlerBeta(handlerMap, threadPoolExecutor));
    }
}
