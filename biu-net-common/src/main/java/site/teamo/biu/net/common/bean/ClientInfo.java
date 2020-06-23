package site.teamo.biu.net.common.bean;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.exception.IllegalInformationException;

import java.util.UUID;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientInfo {

    private String name;
    private String key;
    private ChannelHandlerContext channelHandlerContext;
    private int online = YesNo.NO.type;

    public void check() throws IllegalInformationException {
        if(StringUtils.isBlank(key)){
            throw new IllegalInformationException("客户端key不允许为空");
        }
        if(StringUtils.isBlank(name)){
            name = UUID.randomUUID().toString();
        }

    }
}
