package site.teamo.biu.net.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.core.ClientInfoContainer;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetServerHandler1 extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetServerHandler1.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            ClientInfo clientInfo = ClientInfoContainer.get(buf.toString(Charset.defaultCharset()));
            if (clientInfo == null) {
                LOGGER.info("当前连接未注册");
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                return;
            }
            Attribute<String> attr = ctx.channel().attr(BiuNetConstant.clientKey);
            attr.setIfAbsent(buf.toString(Charset.defaultCharset()));
            clientInfo.setChannelHandlerContext(ctx);
            ctx.writeAndFlush(Unpooled.copiedBuffer("注册成功!".getBytes(Charset.defaultCharset())));
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
