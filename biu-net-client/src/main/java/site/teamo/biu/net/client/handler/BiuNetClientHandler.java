package site.teamo.biu.net.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.client.proxy.BiuNetProxyClient;
import site.teamo.biu.net.common.bean.ClientPassport;
import site.teamo.biu.net.common.bean.ProxyClientInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.handler.BiuNetMessageCodec;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.LoginRequest;
import site.teamo.biu.net.common.message.LoginResponse;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetClientHandler extends SimpleChannelInboundHandler<BiuNetMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetClientHandler.class);

    public static ChannelHandlerContext channelHandlerContext;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Attribute<LoginRequest.SimpleProtocol> attr = ctx.channel().attr(BiuNetConstant.LOGIN_REQUEST_PROTOCOL);
        LoginRequest.SimpleProtocol simpleProtocol = attr.get();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setProtocol(simpleProtocol);
        ctx.writeAndFlush(BiuNetMessageCodec.messageToByteBuf(loginRequest));
        channelHandlerContext = ctx;
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if(((ByteBuf)msg).toString(Charset.defaultCharset()).contains("注册")){
//            ctx.fireChannelRead(msg);
//            return;
//        }
//        BiuNetProxyClient.open(ProxyClientInfo.builder()
//                .targetHost("localhost")
//                .targetPort(80)
//                .channelHandlerContext(ctx)
//                .targetContent((ByteBuf)msg)
//                .build()).start();
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BiuNetMessage msg) throws Exception {
        BiuNetMessageHead head = msg.getHeadEnum();
        switch (head){
            case LOGIN_RESPONSE:
                LoginResponse.SimpleProtocol simpleProtocol = LoginResponse.SimpleProtocol.parse(msg.getProtocol());
                LOGGER.info("客户端{}登陆{}",simpleProtocol.getName(),simpleProtocol.getIsSuccess()==1?"成功":"失败");
                break;
            case FORWARD_REQUEST:
                break;
            default:
                ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("",cause);
        ctx.close();
    }
}
