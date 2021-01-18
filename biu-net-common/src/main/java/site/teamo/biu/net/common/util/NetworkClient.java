package site.teamo.biu.net.common.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/16
 */
@Slf4j
public class NetworkClient extends AbstractNetworkService {
    public final String host;
    public final int port;
    public final String name;
    private final ChannelInitializer initializer;
    @Getter
    private Channel channel;

    /**
     * 创建一个客户端
     *
     * @param host        将要连接的主机名
     * @param port        将要连接的主机端口号
     * @param initializer Client的初始化器
     */
    public NetworkClient(String host, int port, ChannelInitializer initializer) {
        this("client-" + host + "-" + port + "-" + System.currentTimeMillis(), host, port, initializer);
    }

    /**
     * 创建客户端
     *
     * @param name        客户端名称
     * @param host        将要连接的主机名
     * @param port        将要连接的主机端口号
     * @param initializer Client的初始化器
     */
    public NetworkClient(String name, String host, int port, ChannelInitializer initializer) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.initializer = initializer;
    }

    /**
     * 使用构建者创建一个客户端
     *
     * @param builder
     */
    public NetworkClient(Builder builder) {
        this.name = builder.name;
        this.host = builder.host;
        this.port = builder.port;
        this.initializer = builder.initializer;
    }

    /**
     * 启动一个客户端
     *
     * @param permanent
     */
    @Override
    public Callable<Boolean> start0(boolean permanent) {
        return () -> {
            log.info("Start client[{}] connect to {}:{}", name, host, port);
            boolean first = true;
            /**
             * 当前的客户端状态为启动时进行循环
             */
            while (Status.STARTED.equals(status)) {
                try {
                    if (!first) {
                        log.info("Retry connect to {}:{} after 5 seconds", host, port);
                        Thread.sleep(5000L);
                    }
                    first = false;
                    EventLoopGroup group = new NioEventLoopGroup();
                    try {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(group)
                                .channel(NioSocketChannel.class)
                                .handler(initializer);

                        ChannelFuture future = bootstrap.connect(host, port).sync();
                        Channel channel = future.channel();
                        this.channel = channel;
                        channel.closeFuture().sync();
                    } finally {
                        group.shutdownGracefully();
                    }
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
                            log.error("The client[{}] connect to {}:{} failed", name, host, port, e);
                        } else {
                            log.info("The client[{}] connect to {}:{} failed", name, host, port);
                        }
                    } else {
                        /**
                         * 单次运行的Server则启动失败时打印错误堆栈信息
                         */
                        status = Status.STOPPED;
                        log.error("The client[{}] connect to {}:{} failed with permanent = {}", name, port, e, permanent);
                    }
                }
            }
            log.info("The client[name: {}, target: {}:{}] stopped", name, host, port);
            return true;
        };
    }

    public synchronized void shutdown() {
        try {
            Boolean result = shutdown0();
            log.info("shutdown client[name: {}, target: {}:{}] result: ", name, host, port, result);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("shutdown client[name: {}, target: {}:{}] error", name, port, e);
            } else {
                log.warn("shutdown client[name: {}, target: {}:{}] error message: {}", name, port, e.getMessage());
            }
        }
    }

    @Override
    public Status status() {
        return status;
    }

    @Data
    @Accessors(fluent = true)
    public static class Builder {
        private String name;
        private String host;
        private int port;
        private ChannelInitializer initializer;

        public static Builder create() {
            return new Builder();
        }

        public NetworkClient buildClient() {
            return new NetworkClient(this);
        }
    }
}
