package site.teamo.biu.net.common.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class PrintDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            try {
                BiuNetMessageHead.valueOfByType(buf.getInt(0));
//                LOGGER.info("message[ {} byte]\nhead:{}", buf.readableBytes(), buf.getInt(0));
            }catch (IllegalArgumentException e){
                LOGGER.info("message[ {} byte] \n {}", buf.readableBytes(), buf.toString(Charset.defaultCharset()));
            }
        }
        ctx.fireChannelRead(msg);
    }
}
