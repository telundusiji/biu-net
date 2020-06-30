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
@AllArgsConstructor
@Builder
public class SessionInfo {

    private String id;

    private ChannelHandlerContext channelHandlerContext;

    public SessionInfo(){
        this.id = UUID.randomUUID().toString();
    }

}
