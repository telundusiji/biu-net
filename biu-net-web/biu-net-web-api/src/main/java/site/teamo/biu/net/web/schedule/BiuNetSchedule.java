package site.teamo.biu.net.web.schedule;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.teamo.biu.net.common.core.MappingContainer;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Component
@EnableScheduling
public class BiuNetSchedule {

    /**
     * 刷新映射容器中客户端和映射关系信息
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void refreshMappingContainer(){
        MappingContainer.refresh();
    }
}
