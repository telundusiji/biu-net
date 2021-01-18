package site.teamo.biu.net.common.coder;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.message.BiuNetMessage;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class BiuNetMessageEncoder extends MessageToByteEncoder<BiuNetMessage> {

    private String name;

    public BiuNetMessageEncoder(String name) {
        this.name = name;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BiuNetMessage biuNetMessage, ByteBuf byteBuf) throws Exception {
        byte[] data = JSON.toJSONBytes(biuNetMessage);
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
        if (log.isDebugEnabled()) {
            log.info("{} encode data {} bytes", name, data.length);
        }
    }
}
