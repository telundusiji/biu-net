package site.teamo.biu.net.common.util;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Component
public class BiuNetApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Getter
    private static EventLoopGroup bossGroup = new NioEventLoopGroup();
    @Getter
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PreDestroy
    public void destroy() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> tClass) {
        return context.getBean(tClass);
    }

    public static BiuNetExecutor executor() {
        return getBean(BiuNetExecutor.class);
    }

    public static void execute(Runnable task) {
        BiuNetExecutor executor = getBean(BiuNetExecutor.class);
        executor.execute(task);
    }
}
