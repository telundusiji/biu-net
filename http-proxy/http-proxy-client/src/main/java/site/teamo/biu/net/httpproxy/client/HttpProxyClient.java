package site.teamo.biu.net.httpproxy.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.util.NetworkClient;
import site.teamo.biu.net.common.util.NetworkServer;
import site.teamo.biu.net.httpproxy.client.init.ClientHandlerInitializer;
import site.teamo.biu.net.httpproxy.client.init.ServerHandlerInitializer;

/**
 * @@author: 爱做梦的锤子
 * @date: 2021/10/13 21:16
 */
public class HttpProxyClient {

    public static int port = 8443;
    public static String serverHost = "127.0.0.1";
    public static int serverPort = 8543;

    public static void main(String[] args) {
        NetworkServer client = NetworkServer.builder()
                .name("http proxy client")
                .port(port)
                .initializer(new ServerHandlerInitializer())
                .buildServer();

        client.start(true);
    }


    public static void main1(String[] args) {
        NetworkServer client = NetworkServer.builder()
                .name("http proxy client")
                .port(port)
                .initializer(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
//                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4))
                                .addLast(new ByteArrayDecoder())
                                .addLast(new TestHandler())
                                .addLast(new ByteArrayEncoder());
//                                .addLast(new LengthFieldPrepender(4));

                    }
                })
                .buildServer();

        client.start(true);
    }

    @Slf4j
    private static class TestHandler extends ChannelInboundHandlerAdapter {

        private NetworkClient client;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            byte[] bytes = byte[].class.cast(msg);
            log.info(new String(bytes));
        }


        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.info("Client channel inactive: {}", ctx.channel().id().asLongText());
            super.channelInactive(ctx);
        }
    }
}
