package site.teamo.biu.net.learning.netty.first;

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
public class TimeServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new TimeServer().bind(port);
    }

    public void bind(int port) throws InterruptedException {

        /**
         * 创建了两个 NioEventLoopGroup 实例。
         * NioEventLoopGroup 是个线程组， 它包含了一组 NIO 线程， 专门用于网络事件的处理，
         * 实际上它们就是 Reactor 线程组。
         */
        //接受客户端请求
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        //对SocketChannel进行读写
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //启动NIO服务端的辅助启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    //设置backlog为1024
                    .option(ChannelOption.SO_BACKLOG,1024)
                    //绑定IO事件的处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeServerHandler());
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
