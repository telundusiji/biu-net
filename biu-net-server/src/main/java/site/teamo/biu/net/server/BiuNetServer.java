package site.teamo.biu.net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetServer {
    private int port;

    private static BiuNetServer biuNetServer;

    private BiuNetServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup slaveGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup,slaveGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new BiuNetServerHandlerInitializer());

            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        }finally {
            masterGroup.shutdownGracefully();
            slaveGroup.shutdownGracefully();
        }

    }

    public static BiuNetServer open(int port) throws InterruptedException {
        synchronized (BiuNetServer.class) {
            if (biuNetServer == null) {
                biuNetServer = new BiuNetServer(port);
            }
        }
        return biuNetServer;
    }
}
