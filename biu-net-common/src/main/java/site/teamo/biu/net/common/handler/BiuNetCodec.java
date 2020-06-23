package site.teamo.biu.net.common.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import site.teamo.biu.net.common.bean.BiuNetMessage;
import site.teamo.biu.net.common.constant.BiuNetConstant;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetCodec extends ByteToMessageCodec<BiuNetMessage> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int head = in.readInt();
        String clientKey = (String) in.readCharSequence(36, Charset.defaultCharset());
        String sessionId = (String) in.readCharSequence(36, Charset.defaultCharset());
        byte[] content = new byte[in.readableBytes()];
        in.readBytes(content);
        out.add(BiuNetMessage.builder()
                .head(head)
                .clientKey(clientKey.toCharArray())
                .sessionId(sessionId.toCharArray())
                .content(content)
                .build());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, BiuNetMessage msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getHead());
        out.writeCharSequence(new String(msg.getClientKey()), Charset.defaultCharset());
        out.writeCharSequence(new String(msg.getSessionId()), Charset.defaultCharset());
        out.writeBytes(msg.getContent());
    }

    public static ByteBuf messageToByteBuf(BiuNetMessage message) {
        ByteBuf buf = Unpooled.buffer(64, 1024);
        buf.writeInt(BiuNetConstant.register_head);
        buf.writeCharSequence(new String(message.getClientKey()), Charset.defaultCharset());
        buf.writeCharSequence(new String(message.getSessionId()), Charset.defaultCharset());
        buf.writeBytes(message.getContent());
        return buf;
    }
}
