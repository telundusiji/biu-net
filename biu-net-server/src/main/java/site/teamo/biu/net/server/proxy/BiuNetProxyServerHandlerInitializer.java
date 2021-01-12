package site.teamo.biu.net.server.proxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.handler.PrintDecoder;
import site.teamo.biu.net.server.proxy.handler.BiuNetProxyServerHandler;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetProxyServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ProxyServerInfo proxyServerInfo;

    public BiuNetProxyServerHandlerInitializer(ProxyServerInfo proxyServerInfo) {
        this.proxyServerInfo = proxyServerInfo;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        Attribute<ProxyServerInfo> attr = ch.attr(BiuNetConstant.proxyServerInfo);
        attr.setIfAbsent(proxyServerInfo);
        ch.pipeline()
                .addLast(new PrintDecoder())
                .addLast(new BiuNetProxyServerHandler());
    }


}
