package pers.simuel.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.enums.PackageType;
import pers.simuel.rpc.enums.RPCError;
import pers.simuel.rpc.exceptions.RPCException;
import pers.simuel.rpc.protocol.RPCRequest;
import pers.simuel.rpc.protocol.RPCResponse;
import pers.simuel.rpc.serializer.CommonSerializer;

import java.util.List;

/**
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
@Slf4j
public class CommonDecoder extends ByteToMessageDecoder {

    private static final int MAGIC_NUMBER = 0x0522;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 判断是否是我们自己的协议包
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            log.error("无法识别的协议包：{}", magic);
            throw new RPCException(RPCError.UNKNOWN_PROTOCOL);
        }
        // 读取协议包类型(反序列化时需要使用)
        int packageType = in.readInt();
        Class<?> classType;
        if (packageType == PackageType.REQUEST_PACK.getCode()) {
            classType = RPCRequest.class;
        } else if (packageType == PackageType.RESPONSE_PACK.getCode()) {
            classType = RPCResponse.class;
        } else {
            log.error("无法识别的数据包类型:{}", packageType);
            throw new RPCException(RPCError.UNKNOWN_PACKAGE_TYPE);
        }
        // 读取序列化使用的方式
        int serializerType = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByType(serializerType);
        // 读取长度和数据
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        // 进行反序列化操作
        Object obj = serializer.deserialize(classType, bytes);
        out.add(obj);
    }
}
