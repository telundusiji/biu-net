package site.teamo.biu.net.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.util.Attribute;
import site.teamo.biu.net.client.handler.BiuNetClientHandler;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.handler.BiuNetMessageCodec;
import site.teamo.biu.net.common.handler.PrintDecoder;
import site.teamo.biu.net.common.message.LoginRequest;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetClientHandlerInitializer extends ChannelInitializer {

    private LoginRequest.SimpleProtocol protocol;

    public BiuNetClientHandlerInitializer(String name,String password){
        this.protocol = LoginRequest.SimpleProtocol.builder()
                .name(name)
                .password(password)
                .build();
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        Attribute<LoginRequest.SimpleProtocol> attr = ch.attr(BiuNetConstant.LOGIN_REQUEST_PROTOCOL);
        attr.setIfAbsent(protocol);
        ch.pipeline()
                .addLast(new PrintDecoder())
                .addLast(new BiuNetMessageCodec())
                .addLast(new BiuNetClientHandler());
    }
}
