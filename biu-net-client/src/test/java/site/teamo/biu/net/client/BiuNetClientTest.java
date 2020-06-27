package site.teamo.biu.net.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;
import site.teamo.biu.net.client.proxy.BiuNetProxyClient;
import site.teamo.biu.net.common.bean.ProxyClientInfo;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetClientTest {

    @Test
    public void open() throws InterruptedException {
        BiuNetClient.open("localhost",8080,"abc","15a3303e-3f4d-4cac-9bc2-9757f093d759").start();
    }

    @Test
    public void test() throws InterruptedException {
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup slave = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(master,slave)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                BiuNetProxyClient.open(ProxyClientInfo.builder()
                                        .targetHost("localhost")
                                        .targetPort(80)
                                        .channelHandlerContext(ctx)
                                        .targetContent(Unpooled.copiedBuffer(((ByteBuf) msg)))
                                        .build()).start();
                                System.out.println(123);
                            }
                        });
                    }
                });
        ChannelFuture future = bootstrap.bind(8082).sync();
        future.channel().closeFuture().sync();


    }
}