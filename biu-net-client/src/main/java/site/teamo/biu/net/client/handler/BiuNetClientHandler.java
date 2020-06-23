package site.teamo.biu.net.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.client.proxy.BiuNetProxyClient;
import site.teamo.biu.net.common.bean.BiuNetMessage;
import site.teamo.biu.net.common.bean.ProxyClientInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.handler.BiuNetCodec;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetClientHandler.class);

    public static ChannelHandlerContext channelHandlerContext;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Attribute<String> attr = ctx.channel().attr(BiuNetConstant.clientKey);
        String key = attr.get();
        ctx.writeAndFlush(BiuNetCodec.messageToByteBuf(BiuNetMessage.makeRegisterMessage(key.toCharArray())));
        channelHandlerContext = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(((ByteBuf)msg).toString(Charset.defaultCharset()).contains("注册")){
            ctx.fireChannelRead(msg);
            return;
        }
        BiuNetProxyClient.open(ProxyClientInfo.builder()
                .targetHost("localhost")
                .targetPort(80)
                .channelHandlerContext(ctx)
                .targetContent((ByteBuf)msg)
                .build()).start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("",cause);
        ctx.close();
    }
}
