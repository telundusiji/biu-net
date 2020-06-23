package site.teamo.biu.net.server.proxy.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.core.ClientInfoContainer;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetProxyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Attribute<ProxyServerInfo> attr = ctx.channel().attr(BiuNetConstant.proxyServerInfo);
        ClientInfo clientInfo = ClientInfoContainer.get(attr.get().getClientKey());
        if(clientInfo==null){
            ctx.writeAndFlush(Unpooled.copiedBuffer("client不在线！".getBytes(Charset.defaultCharset())))
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }
        clientInfo.getChannelHandlerContext().writeAndFlush(Unpooled.copiedBuffer((ByteBuf) msg));
    }

}
