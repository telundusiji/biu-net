package site.teamo.biu.net.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.util.Attribute;
import site.teamo.biu.net.client.handler.BiuNetClientHandler;
import site.teamo.biu.net.common.constant.BiuNetConstant;
import site.teamo.biu.net.common.core.ClientInfoContainer;
import site.teamo.biu.net.common.handler.PrintDecoder;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetClientHandlerInitializer extends ChannelInitializer {

    private String key;

    public BiuNetClientHandlerInitializer(String key){
        this.key = key;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        Attribute<String> attr = ch.attr(BiuNetConstant.clientKey);
        attr.setIfAbsent(key);
        ch.pipeline()
                .addLast(new PrintDecoder())
                .addLast(new BiuNetClientHandler());
    }
}
