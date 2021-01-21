package site.teamo.biu.net.client.core;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.annoation.Sensitive;
import site.teamo.biu.net.common.info.ClientInfo;
import site.teamo.biu.net.client.core.init.ClientHandlerInitializer;
import site.teamo.biu.net.common.util.NetworkClient;
import site.teamo.biu.net.common.util.RSAUtil;

import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
@Accessors(chain = true)
public class Client {

    @Getter
    private final Info info;

    @Getter
    private final NetworkClient networkClient;

    public Client(Info info) {
        this.info = info;
        this.networkClient = NetworkClient.Builder.create()
                .name(info.getName())
                .host(info.getServerHost())
                .port(info.getServerPort())
                .initializer(new ClientHandlerInitializer(this))
                .buildClient();
        info.setStatus(() -> networkClient.status().name());
    }

    public void start() {
        this.networkClient.start(true);
    }


    public void stop() {
        if (this.networkClient != null) {
            this.networkClient.shutdown();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Info {
        private String id;
        private String name;
        @Sensitive
        private String password;
        private String serverHost;
        private Integer serverPort;
        private String publicKey;
        private Supplier<String> status;

        public String getEncryptPassword() throws Exception {
            return RSAUtil.encrypt(password, publicKey, Charset.defaultCharset());
        }

        public String getName() {
            if (StringUtils.isBlank(name)) {
                return String.format("Client[%s-%s:%d]", id, serverHost, serverPort);
            }
            return name;
        }

        public String getStatus() {
            return status.get();
        }

        public Client buildClient() {
            return new Client(this);
        }

        public ClientInfo toInfo() {
            return ClientInfo.builder()
                    .id(id)
                    .name(name)
                    .password(password)
                    .build();
        }
    }
}
