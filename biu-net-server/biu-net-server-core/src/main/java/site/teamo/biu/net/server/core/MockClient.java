package site.teamo.biu.net.server.core;

import io.netty.channel.ChannelHandlerContext;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.util.MD5Util;
import site.teamo.biu.net.common.util.RSAUtil;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Data
@Slf4j
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
        @Getter(AccessLevel.NONE)
        private String privateKey;
        private volatile int online = YesNo.NO.type;
        private long loginTime;

        public boolean checkPassword(String encryptPassword) {
            try {
                String decryptPassword = RSAUtil.decrypt(encryptPassword, privateKey, Charset.defaultCharset());
                return StringUtils.equals(password, MD5Util.toMD5(decryptPassword));
            } catch (Exception e) {
                log.warn("Check password failed", e.getMessage());
                if (log.isDebugEnabled()) {
                    log.error("Check password failed for encryptPassword: {}", encryptPassword, e);
                }
                return false;
            }
        }

        public MockClient buildClient() {
            return new MockClient(this);
        }
    }
}
