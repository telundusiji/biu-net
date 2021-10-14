package site.teamo.biu.net.httpproxy.server.init;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.util.NetworkClient;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new ByteArrayDecoder())
                .addLast(new ByteArrayEncoder())
                .addLast(new ServerHandler());

    }


    private class ServerHandler extends ChannelInboundHandlerAdapter {

        private boolean isHttps;

        private boolean hasConnectTarget;

        private NetworkClient client;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (hasConnectTarget) {
                client.getChannelSync().writeAndFlush(msg);
                return;
            }
            analysisData(byte[].class.cast(msg),ctx.channel());
            if (!hasConnectTarget) {
                return;
            }
            if (isHttps) {
                ctx.writeAndFlush("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
                return;
            } else {
                client.getChannelSync().writeAndFlush(msg);
            }

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.info("Client channel inactive: {}", ctx.channel().id().asLongText());
            super.channelInactive(ctx);
        }

        public void analysisData(byte[] bytes, Channel channel) {
            String data = new String(bytes);
            String[] lines = data.split("\r\n");
            for (String line : lines) {
                if (line.startsWith("CONNECT")) {
                    isHttps = true;
                }
                if (line.startsWith("Host: ")) {
                    String[] hostPort = line.replace("Host: ", "").split(":");
                    String host = hostPort[0];
                    int port = 80;
                    if (hostPort.length == 2) {
                        port = Integer.parseInt(hostPort[1]);
                    }
                    this.client = NetworkClient.builder()
                            .host(host)
                            .port(port)
                            .initializer(new ClientHandlerInitializer(channel))
                            .buildClient();
                    client.start(true);
                    hasConnectTarget = true;
                    return;
                }
            }
        }
    }
}
