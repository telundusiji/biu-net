package site.teamo.biu.net.server.proxy;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/11
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * @author yx
 * @date 2020/9/22  16:41
 * Description
 */
public class ProxyServer {
    ServerBootstrap serverBootstrap;
    Bootstrap bootstrap;
    EventLoopGroup bossgroup;
    EventLoopGroup workgroup;
    int serverPort = 9999;
    String remoteaddr = "127.0.0.1";
    int remotePort = 4040;

    public ProxyServer() {
        serverBootstrap = new ServerBootstrap();
        bootstrap = new Bootstrap();
        bossgroup = new NioEventLoopGroup();
        workgroup = new NioEventLoopGroup();
        serverBootstrap.group(bossgroup, workgroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(bossgroup);
        //下面的代码为缓冲区设置,其实是非必要代码,可以不用设置,也可以根据自己需求设置参数
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        // SO_SNDBUF发送缓冲区，SO_RCVBUF接收缓冲区，SO_KEEPALIVE开启心跳监测（保证连接有效）
        serverBootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    public ChannelFuture init() {
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel cliCh) throws Exception {
                        cliCh.pipeline().addLast(new DataHandler(ch,"client"));
                    }

                    @Override
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("client channelUnregistered");
                        super.channelUnregistered(ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("client channel Inactive");
                        super.channelInactive(ctx);
                    }
                });
                ChannelFuture sync = bootstrap.connect(remoteaddr, remotePort).sync();
                ch.pipeline().addLast(new DataHandler(sync.channel(),"server"));
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("server channel Inactive");
                super.channelInactive(ctx);
            }

            @Override
            public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                super.channelUnregistered(ctx);
                System.out.println("server channelUnregistered");
            }
        });
        ChannelFuture future = serverBootstrap.bind(serverPort);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // session cleanup logic
                System.out.println("channel 关闭2");
            }

        });
        return future;
    }

    public static void main(String[] args) throws InterruptedException {
        ProxyServer proxyServer = new ProxyServer();
        ChannelFuture init = proxyServer.init();
        try {
            init.sync();
            //注意这里必须写关闭channel的future为同步方法,因为只有主线程结束才会关闭他会一直阻塞在这里,不然当服务启动过后就会结束主线程,整个任务接结束了
            init.channel().closeFuture().sync();
        } finally {
            init.channel().closeFuture().sync();
        }
    }
}

//这里是为了方便展示所以放到一个文件里了,正式使用的时候不建议这么写
@ChannelHandler.Sharable
class DataHandler extends ChannelInboundHandlerAdapter {
    Channel channel;
    String name;

    public DataHandler(Channel channel, String name) {
        this.channel = channel;
        this.name = name;
    }

    /**
     * 业务处理逻辑
     * 用于处理读取数据请求的逻辑。
     * ctx - 上下文对象。其中包含于客户端建立连接的所有资源。 如： 对应的Channel
     * msg - 读取到的数据。 默认类型是ByteBuf，是Netty自定义的。是对ByteBuffer的封装。 因为要把读取到的数据写入另外一个通道所以必须要考虑缓冲区复位问题,不然会报错。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取读取的数据， 是一个缓冲。
        ByteBuf readBuffer = (ByteBuf) msg;
        System.out.println(name + " get data: " + readBuffer.writableBytes());
        //这里的复位不能省略,不然会因为计数器问题报错.
        readBuffer.retain();
        channel.writeAndFlush(readBuffer);
        ReferenceCountUtil.release(msg);

    }

    /**
     * 异常处理逻辑， 当客户端异常退出的时候，也会运行。
     * ChannelHandlerContext关闭，也代表当前与客户端连接的资源关闭。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(name + " exceptionCaught method run...");
        channel.close();
        ctx.close();
        System.out.println(name + " exceptionCaught method finish");
        // cause.printStackTrace();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println(name + " read complete");
    }
}

