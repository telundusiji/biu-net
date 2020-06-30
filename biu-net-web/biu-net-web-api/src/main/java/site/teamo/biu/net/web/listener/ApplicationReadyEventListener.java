package site.teamo.biu.net.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import site.teamo.biu.net.web.init.BiuNetServerStart;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

    @Autowired
    private BiuNetServerStart biuNetServerStart;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        LOGGER.info("启动 BiuNetServer...");
        try {
            biuNetServerStart.init();
            biuNetServerStart.start();
        } catch (Exception e) {
            LOGGER.error("BiuNetServer 启动失败",e);
        }
        LOGGER.info("BiuNetServer 启动成功");
    }
}
