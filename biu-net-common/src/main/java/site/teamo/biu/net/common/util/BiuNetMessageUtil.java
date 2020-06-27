package site.teamo.biu.net.common.util;

import io.netty.buffer.ByteBuf;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public class BiuNetMessageUtil {
    public static void marge(char[] chars,CharSequence charSequence){
        for (int i = 0; i < charSequence.length(); i++) {
            chars[i] = charSequence.charAt(i);
        }
    }

    public static byte[] read(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
