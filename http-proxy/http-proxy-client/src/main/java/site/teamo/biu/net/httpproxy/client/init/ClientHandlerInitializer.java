package site.teamo.biu.net.httpproxy.client.init;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class ClientHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private Channel channel;

    public ClientHandlerInitializer(Channel channel) {
        this.channel = channel;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
//                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4))
                .addLast(new ByteArrayDecoder())
                .addLast(new ByteArrayEncoder())
//                .addLast(new LengthFieldPrepender(4))
                .addLast(new ClientHandler(channel));

    }


    private class ClientHandler extends ChannelInboundHandlerAdapter {

        private Channel channel;

        public ClientHandler(Channel channel) {
            this.channel = channel;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            channel.writeAndFlush(msg);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.info("Client channel inactive: {}", ctx.channel().id().asLongText());
            super.channelInactive(ctx);
        }
    }
}
