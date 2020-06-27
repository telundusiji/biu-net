package site.teamo.biu.net.client.proxy.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import site.teamo.biu.net.common.bean.ProxyClientInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetProxyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Attribute<ProxyClientInfo> attr = ctx.channel().attr(BiuNetConstant.proxyClientInfo);
        attr.get().getChannelHandlerContext().writeAndFlush(Unpooled.copiedBuffer(((ByteBuf)msg)));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Attribute<ProxyClientInfo> attr = ctx.channel().attr(BiuNetConstant.proxyClientInfo);
        ByteBuf targetContent = attr.get().getTargetContent();
        ctx.writeAndFlush(targetContent);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
