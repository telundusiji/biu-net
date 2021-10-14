package site.teamo.biu.net.httpproxy.client.init;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.util.NetworkClient;
import site.teamo.biu.net.httpproxy.client.HttpProxyClient;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
//                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4))
                .addLast(new ByteArrayDecoder())
                .addLast(new ByteArrayEncoder())
//                .addLast(new LengthFieldPrepender(4))
                .addLast(new ServerHandler());

    }


    private class ServerHandler extends ChannelInboundHandlerAdapter {

        private NetworkClient client;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            byte[] bytes = byte[].class.cast(msg);
//            log.info(new String(bytes));
            client.getChannelSync().writeAndFlush(bytes);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            this.client = NetworkClient.builder()
                    .name("http proxy client")
                    .host(HttpProxyClient.serverHost)
                    .port(HttpProxyClient.serverPort)
                    .initializer(new ClientHandlerInitializer(ctx.channel()))
                    .buildClient();
            client.start(true);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.info("Client channel inactive: {}", ctx.channel().id().asLongText());
            super.channelInactive(ctx);
        }
    }
}
