package site.teamo.biu.net.server.core;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import site.teamo.biu.net.common.enums.YesNo;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Data
public class MockClient {

    private Info info;

    private ChannelHandlerContext ctx;

    public MockClient(Info info) {
        this.info = info;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Info {
        private String id;
        private String name;
        private String password;
        private volatile int online = YesNo.NO.type;
        private long loginTime;

        public MockClient buildClient() {
            return new MockClient(this);
        }
    }
}
