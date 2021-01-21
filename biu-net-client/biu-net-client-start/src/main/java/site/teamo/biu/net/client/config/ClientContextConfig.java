package site.teamo.biu.net.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.teamo.biu.net.client.core.Client;
import site.teamo.biu.net.client.core.ClientContext;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/18
 */
@Configuration
public class ClientContextConfig {

    @Value("${biu-net.client-info.id}")
    private String id;
    @Value("${biu-net.client-info.name}")
    private String name;
    @Value("${biu-net.client-info.server-host}")
    private String serverHost;
    @Value("${biu-net.client-info.server-port}")
    private Integer serverPort;
    @Value("${biu-net.client-info.password}")
    private String password;
    @Value("${biu-net.client-info.public-key}")
    private String publicKey;

    @Bean
    public ClientContext clientContext() {
        Client.Info info = Client.Info.builder()
                .id(id)
                .name(name)
                .password(password)
                .serverHost(serverHost)
                .serverPort(serverPort)
                .publicKey(publicKey)
                .build();
        return new ClientContext(info);
    }

}
