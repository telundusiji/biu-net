package site.teamo.biu.net.common.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProxyClientInfo {
    private String targetHost;
    private int targetPort;
    private ByteBuf targetContent;
    private ChannelHandlerContext channelHandlerContext;
}
