package site.teamo.biu.net.learning.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/15
 */
public class MainServer {
    private int port;

    public MainServer(int port){
        this.port = port;
    }

    public void start() throws InterruptedException {


        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    //设置backlog为1024
                    .option(ChannelOption.SO_BACKLOG,1024)
                    //绑定IO事件的处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MainServerHandler());
                        }
                    });

            //绑定端口等待同步成功
            ChannelFuture future = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        }finally {
            masterGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
