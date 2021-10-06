package pers.simuel.trpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageCodec;
import pers.simuel.trpc.entity.RPCRequest;
import pers.simuel.trpc.enumeration.PackageType;
import pers.simuel.trpc.serializers.CommonSerializer;

import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 20:56
 * 自定义编码规则
 * Magic Number: 4字节
 * Package Type: 4字节
 * CommonSerializer Type: 4字节
 * Data Length: 4字节
 * Data: n字节
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0x0522;
    
    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 写入魔数
        out.writeInt(MAGIC_NUMBER);
        // 写入包类型
        if (msg instanceof RPCRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 写入序列化方式
        out.writeInt(serializer.getSerializeType());
        // 写入数据长度（所以得先获取序列化后的数据）
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        // 写入数据
        out.writeBytes(bytes);
    }
}
