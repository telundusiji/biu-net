package site.teamo.biu.net.server.core.init;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.CloseProxyClient;
import site.teamo.biu.net.common.message.PackageData;
import site.teamo.biu.net.server.core.Proxy;
import site.teamo.biu.net.server.core.ProxyServer;
import site.teamo.biu.net.server.core.ServerContext;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class ProxyServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ProxyServer proxyServer;

    public ProxyServerHandlerInitializer(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new ByteArrayDecoder())
                .addLast(new ByteArrayEncoder())
                .addLast(new ProxyServerHandler());
    }


    private class ProxyServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ServerContext serverContext = ServerContext.getInstance();
            Proxy proxy = serverContext.getProxy(proxyServer.getInfo().getId());
            BiuNetMessage<PackageData.Request> message = PackageData.Request.builder()
                    .proxyCtxId(ctx.channel().id().asLongText())
                    .proxyServerPort(proxyServer.getInfo().getPort())
                    .targetHost(proxy.getInfo().getTargetHost())
                    .targetPort(proxy.getInfo().getTargetPort())
                    .data((byte[]) msg)
                    .build().buildData();
            ChannelHandlerContext clientCtx = proxy.getMockClient().getCtx();
            if (clientCtx != null) {
                clientCtx.writeAndFlush(message);
            } else {
                log.warn("Client[{}] is not online", proxy.getMockClient().getInfo().getName());
                if (log.isDebugEnabled()) {
                    log.error("Not found client[{}] for write data from proxyServerPort: {}, proxyCtxId: {}"
                            , proxy.getMockClient().getInfo().getName()
                            , message.getContent().getProxyServerPort()
                            , message.getContent().getProxyCtxId());
                }
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            if (log.isDebugEnabled()) {
                log.info("Proxy server channel active: {}", ctx.channel().id().asLongText());
            }
            proxyServer.addCtx(ctx);
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (log.isDebugEnabled()) {
                log.info("Proxy sever channel inactive: {}", ctx.channel().id().asLongText());
            }
            proxyServer.remove(ctx);
            notifyToCloseProxyClient(ctx.channel().id().asLongText());
            super.channelInactive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
            notifyToCloseProxyClient(ctx.channel().id().asLongText());
        }


        private void notifyToCloseProxyClient(String proxyCtxId) {
            ServerContext serverContext = ServerContext.getInstance();
            Proxy proxy = serverContext.getProxy(proxyServer.getInfo().getId());
            proxy.getMockClient().getCtx().writeAndFlush(CloseProxyClient.Data.buildData(proxyCtxId));
        }
    }
}

