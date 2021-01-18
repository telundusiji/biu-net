package site.teamo.biu.net.server.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.info.PublicServerInfo;
import site.teamo.biu.net.server.core.init.PublicServerHandlerInitializer;
import site.teamo.biu.net.common.util.NetworkServer;

import java.util.Date;
import java.util.function.Supplier;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Data
@Slf4j
public class PublicServer {

    private final Info info;
    private final NetworkServer networkServer;

    public PublicServer(Info info) {
        this.info = info;
        networkServer = NetworkServer.Builder.create()
                .name(info.getName())
                .port(info.getPort())
                .initializer(new PublicServerHandlerInitializer(this))
                .buildServer();
        info.setStatus(() -> networkServer.status().name());
    }

    public void start() {
        if (networkServer.isStarted()) {
            return;
        }
        this.networkServer.start(true);
        info.setStartTime(new Date());
    }

    public void stop() {
        if (networkServer != null) {
            networkServer.shutdown();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Info {
        private String name;
        private Integer port;
        private Date startTime;
        private Supplier<String> status;

        public String getName() {
            if (StringUtils.isBlank(name)) {
                return String.format("PublicServer[%d]", port);
            }
            return name;
        }

        public String getStatus() {
            return status.get();
        }

        public PublicServer buildPublicServer() {
            return new PublicServer(this);
        }

        public PublicServerInfo toInfo() {
            return PublicServerInfo.builder()
                    .port(port)
                    .build();
        }
    }
}
