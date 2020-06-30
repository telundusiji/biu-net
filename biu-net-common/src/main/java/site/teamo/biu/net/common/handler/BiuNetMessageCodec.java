package site.teamo.biu.net.common.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.message.*;
import site.teamo.biu.net.common.util.BiuNetMessageUtil;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetMessageCodec extends ByteToMessageCodec<BiuNetMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetMessageCodec.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        int head = in.readInt();
        BiuNetMessageHead biuNetMessageHead = BiuNetMessageHead.valueOfByType(head);
        BiuNetMessage biuNetMessage = null;
        switch (biuNetMessageHead) {
            case PING_REQUEST:
                break;
            case PING_RESPONSE:
                break;
            case LOGIN_REQUEST:
                biuNetMessage = new LoginRequest();
                break;
            case LOGIN_RESPONSE:
                biuNetMessage = new LoginResponse();
                break;
            case FORWARD_REQUEST:
                biuNetMessage = new ForwardRequest();
                break;
            case FORWARD_RESPONSE:
                biuNetMessage = new ForwardResponse();
                break;
        }
        biuNetMessage.readFromByteBuf(in);
        biuNetMessage.setContent(BiuNetMessageUtil.read(in));
        LOGGER.info("message[ {} byte]\n{}",readableBytes,biuNetMessage.toString(Charset.defaultCharset()));
        out.add(biuNetMessage);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, BiuNetMessage msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getHead());
        msg.writeToByteBuf(out);
        out.writeBytes(msg.getContent());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(LOGGER.isDebugEnabled()){
            LOGGER.error("消息转换异常",cause);
        }else {
            LOGGER.error("消息转换异常：{}",cause.getMessage());
        }
        ctx.close();
    }

    public static ByteBuf messageToByteBuf(BiuNetMessage message) {
        ByteBuf buf = Unpooled.buffer(64, Integer.MAX_VALUE);
        buf.writeInt(message.getHead());
        message.writeToByteBuf(buf);
        buf.writeBytes(message.getContent());
        return buf;
    }
}
