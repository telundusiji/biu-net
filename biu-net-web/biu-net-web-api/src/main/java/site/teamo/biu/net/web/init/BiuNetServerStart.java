package site.teamo.biu.net.web.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import site.teamo.biu.net.server.BiuNetServer;
import site.teamo.biu.net.web.service.ClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */

@Component
public class BiuNetServerStart {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiuNetServerStart.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ClientService clientService;

    private ExecutorService executorService;

    private List<BiuNetServer> biuNetServers = new ArrayList<>();

    public void init() throws Exception {
        biuNetServers.add(BiuNetServer.open(Integer.valueOf(environment.getProperty("biu-net.server.port","8081"))));
        clientService.loadClient();
    }

    public void start(){
        int size = biuNetServers.size();
        if(size==0){
            return;
        }
        executorService = Executors.newFixedThreadPool(size);
        for (BiuNetServer biuNetServer : biuNetServers) {
            executorService.submit(()->{
                try {
                    biuNetServer.start();
                } catch (InterruptedException e) {
                    LOGGER.error("BiuNetServer exception",e);
                }
            });
        }
    }
}
