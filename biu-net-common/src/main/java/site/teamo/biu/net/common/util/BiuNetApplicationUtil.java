package site.teamo.biu.net.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Component
public class BiuNetApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext context;

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
