package site.teamo.biu.net.learning.netty.http.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/15
 */
public class MsgConvert {
    public static String convertFormByteBuf(Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        return new String(req, Charset.defaultCharset());
    }

    public static ByteBuf convertFormString(Object msg) {
        return Unpooled.copiedBuffer(String.valueOf(msg).getBytes());
    }

    public static byte[] convertFormByteBufToByteArray(Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        return req;
    }

    public static ByteBuf convertFormByteBufToByteBuf(Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        return Unpooled.copiedBuffer(req);
    }
}
