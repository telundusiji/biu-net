package site.teamo.biu.net.server.core.init;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.coder.BiuNetMessageDecoder;
import site.teamo.biu.net.common.coder.BiuNetMessageEncoder;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.Login;
import site.teamo.biu.net.common.message.MessageType;
import site.teamo.biu.net.common.message.PackageData;
import site.teamo.biu.net.common.util.HandlerUtil;
import site.teamo.biu.net.server.core.MockClient;
import site.teamo.biu.net.server.core.Proxy;
import site.teamo.biu.net.server.core.PublicServer;
import site.teamo.biu.net.server.core.ServerContext;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class PublicServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private PublicServer publicServer;

    public PublicServerHandlerInitializer(PublicServer publicServer) {
        this.publicServer = publicServer;
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4))
                .addLast(new BiuNetMessageDecoder(publicServer.getInfo().getName()))
                .addLast(new BiuNetMessageEncoder(publicServer.getInfo().getName()))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new PublicServerHandler());
    }


    private class PublicServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            BiuNetMessage message = BiuNetMessage.class.cast(msg);
            if (!MessageType.LOGIN_REQUEST.equals(message.getType()) && ServerContext.getInstance().getClient(ctx.channel().id().asLongText()) == null) {
                log.info("The message comes from a device that is not logged in");
                ctx.disconnect();
                ctx.close();
                return;
            }
            switch (message.getType()) {
                case PING:
                    HandlerUtil.handlePingMessage(ctx, message, true);
                    break;
                case LOGIN_REQUEST:
                    handleLoginRequest(ctx, message);
                    break;
                case PACKAGE_DATA_RESPONSE:
                    handlePackageDataResponse(ctx, message);
                    break;
                default:
                    HandlerUtil.handleUnknownMessage(ctx, message);
                    break;
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            MockClient client = ServerContext.getInstance().getClient(ctx.channel().id().asLongText());
            if (client != null) {
                log.info("{} logout for {}", client.getInfo().getName(), publicServer.getInfo().getName());
                ServerContext.getInstance().offLine(ctx.channel().id().asLongText());
                return;
            }
            ctx.disconnect();
            ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            MockClient client = ServerContext.getInstance().getClient(ctx.channel().id().asLongText());
            if (client != null) {
                log.info("{} disconnect for {}", client.getInfo().getName(), publicServer.getInfo().getName());
                ServerContext.getInstance().offLine(ctx.channel().id().asLongText());
                return;
            }
            ctx.disconnect();
            ctx.close();
        }

        /**
         * 处理登陆结果
         *
         * @param ctx
         * @param message
         * @return
         */
        public void handleLoginRequest(ChannelHandlerContext ctx, BiuNetMessage<Login.Request> message) {
            String id = message.getContent().getId();
            String password = message.getContent().getPassword();
            ServerContext serverContext = ServerContext.getInstance();
            MockClient client = serverContext.getClient(id);
            if (client == null || !client.getInfo().checkPassword(password)) {
                if (log.isDebugEnabled()) {
                    log.info("Client login with wrong id or password");
                }
                ctx.writeAndFlush(Login.Response.failed("Wrong id or password"));
                ctx.disconnect();
                ctx.close();
                return;
            }
            serverContext.onLine(id, ctx);
            log.info("Client login {}, {}", id, message.getContent().getName());
            ctx.channel().writeAndFlush(Login.Response.success());
        }

        public void handlePackageDataResponse(ChannelHandlerContext ctx, BiuNetMessage<PackageData.Response> message) {
            ServerContext serverContext = ServerContext.getInstance();
            Proxy proxy = serverContext.getProxy(message.getContent().getProxyServerPort());
            ChannelHandlerContext proxyServerCtx = proxy.getProxyServer().getCtxMap().get(message.getContent().getProxyCtxId());
            if (proxyServerCtx != null) {
                proxyServerCtx.writeAndFlush(message.getContent().getData());
            } else {
                if (log.isDebugEnabled()) {
                    log.warn("proxy server ctx is null for proxyCtxId: {}, proxyServerPort: {}"
                            , message.getContent().getProxyCtxId()
                            , message.getContent().getProxyServerPort());
                }
            }
        }

    }

}
