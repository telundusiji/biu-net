package site.teamo.biu.net.server.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.server.BiuNetServerHandlerInitializer;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetProxyServer {
    private ProxyServerInfo info;

    private BiuNetProxyServer(ProxyServerInfo info) {
        this.info = info;
    }

    public void start() throws InterruptedException {
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup slaveGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup, slaveGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new BiuNetProxyServerHandlerInitializer(info));

            ChannelFuture future = bootstrap.bind(info.getPort()).sync();
            future.channel().closeFuture().sync();
        } finally {
            masterGroup.shutdownGracefully();
            slaveGroup.shutdownGracefully();
        }

    }

    public static BiuNetProxyServer open(ProxyServerInfo proxyServerInfo) throws InterruptedException {
        return new BiuNetProxyServer(proxyServerInfo);
    }
}
