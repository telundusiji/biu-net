package site.teamo.biu.net.server.core;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.server.core.init.ProxyServerHandlerInitializer;
import site.teamo.biu.net.common.util.NetworkServer;

import javax.persistence.Id;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Data
@Slf4j
public class ProxyServer {

    private final Info info;
    private final NetworkServer networkServer;
    private Map<String, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>();

    public ProxyServer(Info info) {
        this.info = info;
        this.networkServer = NetworkServer.Builder.create()
                .name(info.getName())
                .port(info.getPort())
                .initializer(new ProxyServerHandlerInitializer(this))
                .buildServer();
        info.setStatus(() -> networkServer.status().name());
    }

    public void start() {
        if (networkServer.isStarted()) {
            return;
        }
        networkServer.start(true);
    }

    public void stop() {
        if (networkServer != null) {
            networkServer.shutdown();
        }
    }

    public void addCtx(ChannelHandlerContext ctx) {
        ctxMap.put(ctx.channel().id().asLongText(), ctx);
    }

    public void remove(ChannelHandlerContext ctx) {
        ctxMap.remove(ctx.channel().id().asLongText());
        ctx.fireChannelInactive();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Info {
        @Id
        private String id;
        private String name;
        private Integer port;
        private Supplier<String> status;

        public String getName() {
            if (StringUtils.isBlank(name)) {
                return String.format("ProxyServer[%s-%d]", id, port);
            }
            return name;
        }

        public ProxyServer buildProxyServer() {
            return new ProxyServer(this);
        }

        public String getStatus() {
            return status.get();
        }
    }

    public static void main(String[] args) {
        Info.builder()
                .id("123")
                .port(8081)
                .build().buildProxyServer().start();
    }
}
