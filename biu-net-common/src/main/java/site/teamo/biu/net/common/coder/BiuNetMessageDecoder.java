package site.teamo.biu.net.common.coder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import site.teamo.biu.net.common.message.*;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
public class BiuNetMessageDecoder extends ByteToMessageDecoder {

    private String name;

    public BiuNetMessageDecoder(String name) {
        this.name = name;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        int dataLength = byteBuf.readInt();
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        if (log.isDebugEnabled()) {
            log.info("{} decode data {} bytes", name, data.length);
        }
        BiuNetMessage message = null;
        try {
            message = JSON.parseObject(data, BiuNetMessage.class);
            switch (message.getType()) {
                case PING:
                    message.setContent(((JSONObject) message.getContent()).toJavaObject(Ping.Data.class));
                    break;
                case LOGIN_REQUEST:
                    message.setContent(((JSONObject) message.getContent()).toJavaObject(Login.Request.class));
                    break;
                case LOGIN_RESPONSE:
                    message.setContent(((JSONObject) message.getContent()).toJavaObject(Login.Response.class));
                    break;
                case PACKAGE_DATA_REQUEST:
                    message.setContent(((JSONObject) message.getContent()).toJavaObject(PackageData.Request.class));
                    break;
                case PACKAGE_DATA_RESPONSE:
                    message.setContent(((JSONObject) message.getContent()).toJavaObject(PackageData.Response.class));
                    break;
                case CLOSE_PROXY_CLIENT:
                    message.setContent(((JSONObject) message.getContent()).toJavaObject(CloseProxyClient.Data.class));
                    break;
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("{} decode data error", name, e);
            }
            log.info("{} decode unknown data {} bytes", name, data.length);
            message = UnKnown.get();
        }
        out.add(message);
    }
}
