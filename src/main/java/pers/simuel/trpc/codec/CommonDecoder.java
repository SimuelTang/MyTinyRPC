package pers.simuel.trpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.entity.RPCResponse;
import pers.simuel.trpc.enumeration.PackageType;
import pers.simuel.trpc.exceptions.RPCError;
import pers.simuel.trpc.exceptions.RPCException;
import pers.simuel.trpc.serializers.CommonSerializer;

import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 22:04
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {

    private static final int MAGIC_NUMBER = 0x0522;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 读取魔数并判断是否合法
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            log.error("无法识别的协议包：{}", magic);
            throw new RPCException(RPCError.UNKNOWN_PROTOCOL);
        }
        // 读取数据包的类型（请求还是响应）
        Class<?> packageClass;
        int packageType = in.readInt();
        if (packageType == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RPCRequest.class;
        } else if (packageType == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RPCResponse.class;
        } else {
            log.error("无法识别的数据包类型：{}", packageType);
            throw new RPCException(RPCError.UNKNOWN_PACKAGE_TYPE);
        }
        // 读取序列化的类型，获取响应的反序列化对象
        int serializeType = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByType(serializeType);
        // 读取长度和字节数组
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        // 进行反序列化操作
        Object obj = serializer.deserialize(packageClass, bytes);
        out.add(obj);
    }
}
