package site.teamo.biu.net.common.util;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/16
 */
@Slf4j
public class NetworkServer extends AbstractNetworkService {
    public final int port;
    public final String name;
    private final ChannelInitializer initializer;

    public NetworkServer(int port, ChannelInitializer initializer) {
        this(null, port, initializer);
    }

    public NetworkServer(String name, int port, ChannelInitializer initializer) {
        this.name = StringUtils.isBlank(name) ? String.format("server-%d-%d", port, System.currentTimeMillis()) : name;
        this.port = port;
        this.initializer = initializer;
    }

    public NetworkServer(Builder builder) {
        this(builder.name, builder.port, builder.initializer);
    }

    /**
     * 启动一个Server
     *
     * @param permanent
     */
    @Override
    public Callable<Boolean> start0(boolean permanent) {
        return () -> {
            log.info("Start server[{}] on port {}", name, port);
            boolean first = true;
            /**
             * 当前的Server状态为启动时进行循环
             */
            while (Status.STARTED.equals(status)) {
                try {
                    if (!first) {
                        log.info("Retry start public server after 5 seconds");
                        Thread.sleep(5000L);
                    }
                    first = false;
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(BiuNetApplicationUtil.getBossGroup(), BiuNetApplicationUtil.getWorkerGroup())
                            .channel(NioServerSocketChannel.class)
                            .childHandler(initializer);

                    ChannelFuture future = bootstrap.bind(port).sync();
                    future.channel().closeFuture().sync();
                    /**
                     * 当Server启动方式不是长久时，则尝试一次后修改状态为停止，跳出循环
                     */
                    if (!permanent) {
                        status = Status.STOPPED;
                    }
                } catch (Exception e) {
                    if (permanent) {
                        /**
                         * 长久方式运行的Server在启动失败时，若日志级别为debug则打印错误堆栈信息，否则仅打印提示信息
                         */
                        if (log.isDebugEnabled()) {
                            log.error("Start server[{}] on port {} failed", name, port, e);
                        } else {
                            log.info("Start server[{}] on port {} failed", name, port);
                        }
                    } else {
                        /**
                         * 单次运行的Server则启动失败时打印错误堆栈信息
                         */
                        status = Status.STOPPED;
                        log.error("Start server[{}] on port {} failed with permanent = {}", name, port, e);
                    }
                }
            }
            log.info("The server[name: {}, port: {}] stopped");
            return true;
        };
    }

    public synchronized void shutdown() {
        try {
            shutdown0();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("shutdown server[name: {}, port: {}] error", name, port, e);
            } else {
                log.warn("shutdown server[name: {}, port: {}] error message: {}", name, port, e.getMessage());
            }
        }
    }

    @Data
    @Accessors(fluent = true)
    public static class Builder {
        public int port;
        public String name;
        private ChannelInitializer initializer;

        public static Builder create() {
            return new Builder();
        }

        public NetworkServer buildServer() {
            return new NetworkServer(this);
        }
    }
}
