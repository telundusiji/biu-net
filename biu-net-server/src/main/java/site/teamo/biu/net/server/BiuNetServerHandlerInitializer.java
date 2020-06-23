package site.teamo.biu.net.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import site.teamo.biu.net.common.handler.BiuNetCodec;
import site.teamo.biu.net.common.handler.PrintDecoder;
import site.teamo.biu.net.server.handler.BiuNetServerHandler;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetServerHandlerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new PrintDecoder())
                .addLast(new BiuNetCodec())
                .addLast(new BiuNetServerHandler());
    }
}
