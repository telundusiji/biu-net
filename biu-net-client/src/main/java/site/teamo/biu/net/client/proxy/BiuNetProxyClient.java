package site.teamo.biu.net.client.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import site.teamo.biu.net.common.bean.ProxyClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetProxyClient {
    private ProxyClientInfo info;

    private BiuNetProxyClient(ProxyClientInfo info) {
        this.info = info;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new BiuNetProxyClientHandlerInitializer(info));

            ChannelFuture future = bootstrap.connect(info.getTargetHost(),info.getTargetPort()).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

    public static BiuNetProxyClient open(ProxyClientInfo proxyClientInfo) throws InterruptedException {
        return new BiuNetProxyClient(proxyClientInfo);
    }
}
