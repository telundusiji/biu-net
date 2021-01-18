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
import site.teamo.biu.net.common.util.HandlerUtil;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.Login;
import site.teamo.biu.net.common.message.PackageData;
import site.teamo.biu.net.common.util.BiuNetApplicationUtil;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;

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
                .addLast(new BiuNetMessageDecoder(client.getNetworkClient().name))
                .addLast(new BiuNetMessageEncoder(client.getNetworkClient().name))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new ClientHandler());

    }


    private class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            BiuNetMessage message = BiuNetMessage.class.cast(msg);
            switch (message.getType()) {
                case LOGIN_RESPONSE:
                    BiuNetApplicationUtil.execute(handleLoginResponse(ctx, message));
                    break;
                case PACKAGE_DATA_REQUEST:
                    BiuNetApplicationUtil.execute(handlePackageDataRequest(ctx, message));
                    break;
                default:
                    BiuNetApplicationUtil.execute(HandlerUtil.unknown(ctx, message));
                    break;
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.info("Client channel active: {}", ctx.channel().id().asLongText());
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
        public Runnable handleLoginResponse(ChannelHandlerContext ctx, BiuNetMessage<Login.Response> message) {
            return () -> {
                log.info("Client login {}, {}", message.getContent().getResult(), message.getContent().getMsg());
            };
        }

        /**
         * 处理待转发的代理数据
         *
         * @param ctx
         * @param message
         * @return
         */
        public Runnable handlePackageDataRequest(ChannelHandlerContext ctx, BiuNetMessage<PackageData.Request> message) {
            return () -> {
                try {
                    ClientContext context = ClientContext.getInstance();
                    PackageData.Request request = message.getContent();
                    ProxyClient.Info info = BiuNetBeanUtil.copyBean(request, ProxyClient.Info.class);
                    ProxyClient proxyClient = context.getProxyClient(info);
                    proxyClient.sendPackageData(message.getContent(), 30, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }

    }
}
