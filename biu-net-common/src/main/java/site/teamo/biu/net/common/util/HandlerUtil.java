package site.teamo.biu.net.common.util;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.UnKnown;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class HandlerUtil {

    public static Runnable unknown(ChannelHandlerContext ctx, BiuNetMessage<UnKnown> message) {
        return () -> {
            if (log.isDebugEnabled()) {
                log.info("unknown message {} bytes: {}", message.getContent());
            }
        };
    }
}
