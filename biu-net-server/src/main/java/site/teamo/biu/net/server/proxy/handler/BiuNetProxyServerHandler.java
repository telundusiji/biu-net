package site.teamo.biu.net.server.proxy.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.bean.SessionInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.core.MappingContainer;
import site.teamo.biu.net.common.core.SessionContainer;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.handler.BiuNetMessageCodec;
import site.teamo.biu.net.common.message.ForwardRequest;
import site.teamo.biu.net.common.util.BiuNetMessageUtil;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetProxyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetProxyServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        Attribute<ProxyServerInfo> attr = ctx.channel().attr(BiuNetConstant.proxyServerInfo);
        ClientInfo clientInfo = MappingContainer.getClientByClientId(attr.get().getClientId());
        if (clientInfo == null || clientInfo.getOnline() == YesNo.NO.type) {
            LOGGER.info("client:{} 不在线",attr.get().getClientId());
            ctx.writeAndFlush(Unpooled.copiedBuffer("client不在线！".getBytes(Charset.defaultCharset())))
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setChannelHandlerContext(ctx);
        SessionContainer.put(sessionInfo);
        ForwardRequest.SimpleProtocol simpleProtocol = ForwardRequest.SimpleProtocol.builder()
                .targetHost(attr.get().getTargetHost())
                .targetPort(attr.get().getTargetPort())
                .sessionId(sessionInfo.getId())
                .build();
        ForwardRequest request = new ForwardRequest();
        request.setProtocol(simpleProtocol);
        request.setContent(BiuNetMessageUtil.read(buf));
        clientInfo.getChannelHandlerContext().writeAndFlush(BiuNetMessageCodec.messageToByteBuf(request));
    }

}
