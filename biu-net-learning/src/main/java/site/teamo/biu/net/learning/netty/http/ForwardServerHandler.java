package site.teamo.biu.net.learning.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.io.IOUtils;
import site.teamo.biu.net.learning.netty.http.util.MsgConvert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/15
 */
public class ForwardServerHandler extends ChannelInboundHandlerAdapter {

//    @Override
//    public void channelRead(ChannelHandlerContext context, Object msg) {
//        String s = MsgConvert.convertFormByteBuf(msg);
//        s = s.replace("localhost:8081","localhost:80");
//        System.out.println("ForwardServerHandler receive:"+ s);
//        ChannelHandlerContext ctx = MainServerHandler.contextMap.get("client1");
//        ctx.writeAndFlush(Unpooled.copiedBuffer(s.getBytes()));
////        context.writeAndFlush(MsgConvert.convertFormString("已转发"));
//    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        String s = MsgConvert.convertFormByteBuf(msg);
        s = s.replace("localhost:8081", "localhost:80");
        System.out.println("ForwardServerHandler receive:" + s);
        try {
            Socket socket = new Socket("localhost",80);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            out.write(s.getBytes(Charset.defaultCharset()));
            out.flush();
            byte[] read = IOUtils.toByteArray(in);
            System.out.println("---读取："+new String(read));
            socket.close();
            context.writeAndFlush(Unpooled.copiedBuffer(read));
            context.close();
        } catch (IOException e) {
            e.printStackTrace();
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
