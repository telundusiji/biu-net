package site.teamo.biu.net.common.bean;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.exception.IllegalInformationException;

import java.util.Date;
import java.util.Objects;
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

    private String id;
    private String name;
    private String password;
    private ChannelHandlerContext channelHandlerContext;
    private int online = YesNo.NO.type;
    private Date loginTime;

    public void check() throws IllegalInformationException {
        if(StringUtils.isBlank(id)){
            throw new IllegalInformationException("客户端id不允许为空");
        }
        if(StringUtils.isBlank(name)){
            throw new IllegalInformationException("客户端名称不允许为空");
        }
        if(StringUtils.isBlank(password)){
            throw new IllegalInformationException("认证密码不允许为空");
        }

    }

    public Key key(){
        return Key.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key{
        private String id;
        private String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(id, key.id) ||
                    Objects.equals(name, key.name);
        }

        @Override
        public int hashCode() {
            /**
             * 目前没有想到好的办法，是的只有一个值也可以得到相同的散列，只能用常量值
             */
            //TODO:后续有办法了在更改
            return Objects.hash("");
        }
    }
}
