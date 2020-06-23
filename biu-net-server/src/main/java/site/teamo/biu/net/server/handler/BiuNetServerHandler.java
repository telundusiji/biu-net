package site.teamo.biu.net.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.common.bean.BiuNetMessage;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.SessionInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.core.ClientInfoContainer;
import site.teamo.biu.net.common.core.MappingContainer;
import site.teamo.biu.net.common.core.SessionContainer;
import site.teamo.biu.net.common.exception.UnregisteredClientException;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetServerHandler extends SimpleChannelInboundHandler<BiuNetMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BiuNetMessage msg) throws Exception {
        if (msg.getHead() == BiuNetConstant.register_head) {
            try {
                MappingContainer.loginClient(
                        ClientInfo.builder()
                                .key(new String(msg.getClientKey()))
                                .channelHandlerContext(ctx)
                                .build());
                Attribute<String> attr = ctx.channel().attr(BiuNetConstant.clientKey);
                attr.setIfAbsent(new String(msg.getClientKey()));
                ctx.writeAndFlush(Unpooled.copiedBuffer("登陆成功!".getBytes(Charset.defaultCharset())));
            }catch (UnregisteredClientException e){
                LOGGER.error("client登陆失败",e);
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        } else if (msg.getHead() == BiuNetConstant.data_head) {
            String sessionId = new String(msg.getSessionId());
            SessionInfo sessionInfo = SessionContainer.get(sessionId);
            sessionInfo.getChannelHandlerContext()
                    .writeAndFlush(Unpooled.copiedBuffer(msg.getContent()));
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
