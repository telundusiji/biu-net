package site.teamo.biu.net.server;

import org.junit.Test;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.core.MappingContainer;
import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.biu.net.server.proxy.BiuNetProxyServer;

import java.util.UUID;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetServerTest {

    @Test
    public void open() throws InterruptedException, IllegalInformationException {
        MappingContainer.registerClient(ClientInfo.builder()
                .id("15a3303e-3f4d-4cac-9bc2-9757f093d759")
                .name("abc")
                .password("15a3303e-3f4d-4cac-9bc2-9757f093d759")
                .build());
        new Thread(()-> {
            try {
                BiuNetServer.open(8080).start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        BiuNetProxyServer.open(ProxyServerInfo.builder()
                .id("测试代理")
                .port(8081)
                .clientId("15a3303e-3f4d-4cac-9bc2-9757f093d759")
                .build()).start();
    }

    @Test
    public void uuid(){
        System.out.println(ClientInfo.builder()
                .id("15a3303e-3f4d-4cac-9bc2-9757f093d759")
                .name("abc")
                .password("15a3303e-3f4d-4cac-9bc2-9757f093d759")
                .build().key().equals(ClientInfo.builder()
                .name("abc")
                .build().key()));
    }
}