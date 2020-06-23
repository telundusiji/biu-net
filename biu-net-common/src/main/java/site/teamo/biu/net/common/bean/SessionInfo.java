package site.teamo.biu.net.common.bean;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionInfo {

    private String Id = UUID.randomUUID().toString();

    private ChannelHandlerContext channelHandlerContext;

}
