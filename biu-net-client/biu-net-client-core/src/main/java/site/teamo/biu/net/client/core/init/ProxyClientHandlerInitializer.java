package site.teamo.biu.net.client.core.init;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.client.core.ClientContext;
import site.teamo.biu.net.client.core.ProxyClient;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.PackageData;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class ProxyClientHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ProxyClient proxyClient;

    public ProxyClientHandlerInitializer(ProxyClient proxyClient) {
        this.proxyClient = proxyClient;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new ByteArrayEncoder())
                .addLast(new ByteArrayDecoder())
                .addLast(new ProxyClientHandler());

    }


    private class ProxyClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            BiuNetMessage<PackageData.Response> message = PackageData.Response.builder()
                    .proxyCtxId(proxyClient.getInfo().getProxyCtxId())
                    .proxyServerPort(proxyClient.getInfo().getProxyServerPort())
                    .data((byte[]) msg)
                    .build().buildData();

            ClientContext clientContext = ClientContext.getInstance();
            clientContext.getClient().getNetworkClient().getChannel().writeAndFlush(message);
        }
    }
}
