package site.teamo.biu.net.client.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.client.core.init.ProxyClientHandlerInitializer;
import site.teamo.biu.net.common.message.PackageData;
import site.teamo.biu.net.common.util.BiuNetApplicationUtil;
import site.teamo.biu.net.common.util.NetworkClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Slf4j
@Data
public class ProxyClient {

    private final Info info;
    private final NetworkClient networkClient;

    public ProxyClient(Info info) {
        this.info = info;
        networkClient = NetworkClient.Builder.create()
                .name(info.getName())
                .host(info.getTargetHost())
                .port(info.getTargetPort())
                .initializer(new ProxyClientHandlerInitializer(this))
                .buildClient();
        info.setStatus(() -> networkClient.status().name());

    }

    public void start() {
        if (networkClient.isStarted()) {
            return;
        }
        networkClient.start(false);
    }

    public void stop() {
        if (networkClient != null) {
            networkClient.shutdown();
        }
    }

    public void sendPackageData(PackageData.Request request, long timeout, TimeUnit timeUnit) {

        long duration = timeUnit.toMillis(timeout);
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < duration) {
            if (networkClient.getChannel() != null && networkClient.getChannel().isActive()) {
                networkClient.getChannel().writeAndFlush(request.getData());
                break;
            }
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Info {
        private String name;
        private String targetHost;
        private Integer targetPort;
        private Integer proxyServerPort;
        private String proxyCtxId;
        private Supplier<String> status;

        public String getName() {
            if (StringUtils.isBlank(name)) {
                return String.format("ProxyClient[%s:%d]", targetHost, targetPort);
            }
            return name;
        }

        public String getStatus() {
            return status.get();
        }

        public ProxyClient buildClient() {
            return new ProxyClient(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Info info = (Info) o;
            return Objects.equals(proxyCtxId, info.proxyCtxId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(proxyCtxId);
        }
    }
}
