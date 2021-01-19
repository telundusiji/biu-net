package site.teamo.biu.net.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import site.teamo.biu.net.client.core.ClientContext;
import site.teamo.biu.net.common.message.Ping;
import site.teamo.biu.net.common.util.NetUtil;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/18
 */

@Configuration
public class PingConfig {

    @Autowired
    private ClientContext clientContext;

    @Scheduled(cron = "0 * * * * ?")
    public void ping() {
        clientContext.getClient().getNetworkClient().getChannel().writeAndFlush(Ping.Data.buildData(NetUtil.myHost()));
    }

}
