package pers.simuel.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import pers.simuel.rpc.enums.PackageType;
import pers.simuel.rpc.model.RpcRequest;
import pers.simuel.rpc.serializer.CommonSerializer;

/**
 * 给自定的协议进行编码，逆向解析后可以进行校验
 * +---------------+---------------+-----------------+-------------+
 * |  Magic Number |  Package Type | Serializer Type | Data Length |
 * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
 * +---------------+---------------+-----------------+-------------+
 * |                          Data Bytes                           |
 * |                   Length: ${Data Length}                      |
 * +---------------------------------------------------------------+
 *
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 15:44
 */
public class CommonEncoder extends MessageToByteEncoder {
    
    private final CommonSerializer serializer;
    
    private static final int MAGIC_NUMBER = 0x0522;
    
    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // ---处理固定部分---
        // 先写入魔数(4 bytes)
        out.writeInt(MAGIC_NUMBER);
        // 写入这个消息的类型(4 bytes)
        if (msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 写入采用的序列化方式(4 bytes)
        out.writeInt(serializer.getSerializerType());
        // 写入数据的长度(4 bytes)
        byte[] data = serializer.serialize(msg);
        out.writeInt(data.length);
        // ---处理不固定部分---
        // 写入数据(长度不固定)
        out.writeBytes(data);
    }
}
