package site.teamo.biu.net.server.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.teamo.biu.net.server.core.ServerContext;
import site.teamo.biu.net.server.web.dao.ClientMapper;
import site.teamo.biu.net.server.web.dao.ProxyMapper;
import site.teamo.biu.net.server.web.dao.ProxyServerMapper;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/14
 */
@Configuration
public class ServerContextConfig {

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ProxyServerMapper proxyServerMapper;

    @Autowired
    private ProxyMapper proxyMapper;

    @Value("${biu-net.public-server.port}")
    private int publicServerPort;

    @Bean
    public ServerContext serverContext() {

        ServerContext serverContext = new ServerContext(clientMapper.selectAll(), proxyServerMapper.selectAll());
        serverContext.addProxies(proxyMapper.selectAll());
        serverContext.startPublicServer(publicServerPort);
        return serverContext;
    }

}
