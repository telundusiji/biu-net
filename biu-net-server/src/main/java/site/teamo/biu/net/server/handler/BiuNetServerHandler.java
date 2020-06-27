package site.teamo.biu.net.server.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.SessionInfo;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.core.MappingContainer;
import site.teamo.biu.net.common.core.SessionContainer;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.handler.BiuNetMessageCodec;
import site.teamo.biu.net.common.message.BiuNetMessage;
import site.teamo.biu.net.common.message.LoginRequest;
import site.teamo.biu.net.common.message.LoginResponse;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetServerHandler extends SimpleChannelInboundHandler<BiuNetMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BiuNetMessage msg) throws Exception {
        BiuNetMessageHead biuNetMessageHead = msg.getHeadEnum();
        switch (biuNetMessageHead){
            case LOGIN_REQUEST:
                LoginRequest.SimpleProtocol simpleProtocol = JSON.parseObject(msg.getProtocol(), LoginRequest.SimpleProtocol.class);
                LoginResponse loginResponse = new LoginResponse();
                try {
                    MappingContainer.loginClient(ClientInfo.builder()
                            .name(simpleProtocol.getName())
                            .password(simpleProtocol.getPassword())
                            .channelHandlerContext(ctx)
                            .build());
                    loginResponse.setProtocol(LoginResponse.SimpleProtocol.builder()
                            .name(simpleProtocol.getName())
                            .isSuccess(YesNo.YES.type)
                            .build());
                    ctx.writeAndFlush(BiuNetMessageCodec.messageToByteBuf(loginResponse));
                }catch (Exception e){
                    LOGGER.error("{}登陆失败",simpleProtocol.getName(),e);
                    loginResponse.setProtocol(LoginResponse.SimpleProtocol.builder()
                            .name(simpleProtocol.getName())
                            .isSuccess(YesNo.NO.type)
                            .build());
                    ctx.writeAndFlush(BiuNetMessageCodec.messageToByteBuf(loginResponse));
                }
                break;
            case FORWARD_RESPONSE:
                break;
            default:
                ctx.fireChannelRead(msg);
                break;
        }
    }
}
