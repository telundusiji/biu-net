package site.teamo.biu.net.client.core.init;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.client.core.Client;
import site.teamo.biu.net.client.core.ClientContext;
import site.teamo.biu.net.client.core.ProxyClient;
import site.teamo.biu.net.common.coder.BiuNetMessageDecoder;
import site.teamo.biu.net.common.coder.BiuNetMessageEncoder;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.CloseProxyClient;
import site.teamo.biu.net.common.message.Login;
import site.teamo.biu.net.common.message.PackageData;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;
import site.teamo.biu.net.common.util.HandlerUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class ClientHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private Client client;

    public ClientHandlerInitializer(Client client) {
        this.client = client;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4))
                .addLast(new BiuNetMessageDecoder(client.getInfo().getName()))
                .addLast(new BiuNetMessageEncoder(client.getInfo().getName()))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new ClientHandler());

    }


    private class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            BiuNetMessage message = BiuNetMessage.class.cast(msg);
            switch (message.getType()) {
                case PING:
                    HandlerUtil.handlePingMessage(ctx, message, false);
                    break;
                case LOGIN_RESPONSE:
                    handleLoginResponse(ctx, message);
                    break;
                case PACKAGE_DATA_REQUEST:
                    handlePackageDataRequest(ctx, message);
                    break;
                case CLOSE_PROXY_CLIENT:
                    handleCloseProxyClient(ctx, message);
                    break;
                default:
                    HandlerUtil.handleUnknownMessage(ctx, message);
                    break;
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Login.Request.builder()
                    .id(client.getInfo().getId())
                    .name(client.getInfo().getName())
                    .password(client.getInfo().getEncryptPassword())
                    .build()
                    .buildData());
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.info("Client channel inactive: {}", ctx.channel().id().asLongText());
            super.channelInactive(ctx);
        }

        /**
         * 处理登陆结果
         *
         * @param ctx
         * @param message
         * @return
         */
        public void handleLoginResponse(ChannelHandlerContext ctx, BiuNetMessage<Login.Response> message) {
            if (Login.LoginResult.FAILED.equals(message.getContent().getResult())) {
                log.error("Client login {}, {}", message.getContent().getResult(), message.getContent().getMsg());
            } else {
                log.error("Client login {} ", message.getContent().getResult());
            }
        }

        /**
         * 处理待转发的代理数据
         *
         * @param ctx
         * @param message
         * @return
         */
        public void handlePackageDataRequest(ChannelHandlerContext ctx, BiuNetMessage<PackageData.Request> message) {
            try {
                ClientContext context = ClientContext.getInstance();
                PackageData.Request request = message.getContent();
                ProxyClient.Info info = BiuNetBeanUtil.copyBean(request, ProxyClient.Info.class);
                ProxyClient proxyClient = context.getProxyClient(info);
                proxyClient.sendPackageData(message.getContent(), 30, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void handleCloseProxyClient(ChannelHandlerContext ctx, BiuNetMessage<CloseProxyClient.Data> message) {
            ClientContext context = ClientContext.getInstance();
            String proxyCtxId = message.getContent().proxyCtxId;
            ProxyClient proxyClient = context.getAndRemoveProxyClient(proxyCtxId);
            if (proxyClient != null) {
                proxyClient.stop();
            }
        }

    }
}
