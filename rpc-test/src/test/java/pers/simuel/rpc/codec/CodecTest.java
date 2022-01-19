package pers.simuel.rpc.codec;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import pers.simuel.rpc.protocol.RPCRequest;
import pers.simuel.rpc.serializer.JDKSerializer;

/**
 * @Author simuel_tang
 * @Date 2022/1/2
 * @Time 10:27
 */
public class CodecTest {
    @Test
    public void testCodec() {
        // 构造请求用于测试
        RPCRequest request = RPCRequest.builder()
                .interfaceName("pers.simuel.test")
                .methodName("test")
                .parameters(new Object[]{})
                .paraTypes(new Class[]{String.class})
                .build();
        // 通过本地channel进行测试
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new CommonEncoder(new JDKSerializer()));
        
    }
}
