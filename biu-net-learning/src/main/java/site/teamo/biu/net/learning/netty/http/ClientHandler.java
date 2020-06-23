package site.teamo.biu.net.learning.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpResponseEncoder;
import site.teamo.biu.net.learning.netty.http.util.MsgConvert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/15
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private String id;

    public ClientHandler(String id) {
        this.id = id;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer(("register:"+id).getBytes(Charset.defaultCharset())));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String content = MsgConvert.convertFormByteBuf(msg);
        if(content.contains("注册")){
            System.out.println(content);
        }else {
            System.out.println("收到服务端转发内容:");
            System.out.println(content);

            EventLoopGroup group = new NioEventLoopGroup();

            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY,true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        ctx.writeAndFlush(MsgConvert.convertFormByteBufToByteBuf(msg));
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(MsgConvert.convertFormByteBuf(msg));
                                    }
                                });
                            }
                        });
                ChannelFuture future = bootstrap.connect("localhost", 80).sync();
                future.channel().closeFuture().sync();
            }finally {
                group.shutdownGracefully();
            }

            ctx.write(Unpooled.copiedBuffer((id+"收到").getBytes(Charset.defaultCharset())));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
