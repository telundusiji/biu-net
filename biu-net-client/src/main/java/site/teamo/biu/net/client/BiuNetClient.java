package site.teamo.biu.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetClient {

    private String serverHost;
    private int port;
    private String name;
    private String password;


    public BiuNetClient(String serverHost, int port, String name, String password) {
        this.serverHost = serverHost;
        this.port = port;
        this.name = name;
        this.password = password;
    }

    public static BiuNetClient open(String serverHost, int port, String name, String password) {
        return new BiuNetClient(serverHost, port, name, password);
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new BiuNetClientHandlerInitializer(name,password));

            ChannelFuture future = bootstrap.connect(serverHost, port).sync();
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
