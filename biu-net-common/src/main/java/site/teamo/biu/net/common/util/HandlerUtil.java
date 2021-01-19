package site.teamo.biu.net.common.util;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.Ping;
import site.teamo.biu.net.common.message.UnKnown;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class HandlerUtil {

    public static void handleUnknownMessage(ChannelHandlerContext ctx, BiuNetMessage<UnKnown> message) {

        if (log.isDebugEnabled()) {
            log.info("unknown message {} bytes: {}", message.getContent());
        }
    }

    /**
     * 处理心跳数据
     *
     * @param ctx
     * @param message
     * @return
     */
    public static void handlePingMessage(ChannelHandlerContext ctx, BiuNetMessage<Ping.Data> message, boolean reply) {
        if (log.isDebugEnabled()) {
            log.info("ping data from: {}", message.getContent().host);
        }
        if (reply) {
            ctx.writeAndFlush(Ping.Data.buildData(NetUtil.myHost()));
        }
    }
}
