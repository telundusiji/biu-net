package site.teamo.biu.net.server;

import org.junit.Test;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.core.ClientInfoContainer;
import site.teamo.biu.net.common.core.MappingContainer;
import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.biu.net.server.proxy.BiuNetProxyServer;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetServerTest {

    @Test
    public void open() throws InterruptedException, IllegalInformationException {
        MappingContainer.registerClient(ClientInfo.builder()
                .name("测试客户端")
                .key("15a3303e-3f4d-4cac-9bc2-9757f093d759")
                .build());
        new Thread(()-> {
            try {
                BiuNetServer.open(8080).start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        BiuNetProxyServer.open(ProxyServerInfo.builder()
                .name("测试代理")
                .port(8081)
                .clientKey("15a3303e-3f4d-4cac-9bc2-9757f093d759")
                .build()).start();
    }

    @Test
    public void uuid(){
        System.out.println(UUID.randomUUID().toString());
    }
}