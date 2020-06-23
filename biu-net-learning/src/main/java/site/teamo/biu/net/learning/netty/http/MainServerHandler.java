package site.teamo.biu.net.learning.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import site.teamo.biu.net.learning.netty.http.util.MsgConvert;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/15
 */
public class MainServerHandler extends ChannelInboundHandlerAdapter {

    public static Map<String,ChannelHandlerContext> contextMap = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MainServerHandler 启动!");
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        String content = MsgConvert.convertFormByteBuf(msg);
        if(content.startsWith("register")){
            System.out.println(content);
            String id = content.split(":")[1];
            contextMap.put(id,context);
            context.write(Unpooled.copiedBuffer((id+"注册成功!").getBytes()));
        }else{

            System.out.println(content);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
