package site.teamo.biu.net.client.proxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import site.teamo.biu.net.client.proxy.handler.BiuNetProxyClientHandler;
import site.teamo.biu.net.common.bean.ProxyClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.handler.PrintDecoder;


/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetProxyClientHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ProxyClientInfo proxyClientInfo;

    public BiuNetProxyClientHandlerInitializer(ProxyClientInfo proxyClientInfo) {
        this.proxyClientInfo = proxyClientInfo;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        Attribute<ProxyClientInfo> attr = ch.attr(BiuNetConstant.proxyClientInfo);
        attr.setIfAbsent(proxyClientInfo);
        ch.pipeline()
                .addLast(new PrintDecoder())
                .addLast(new BiuNetProxyClientHandler());
    }
}
