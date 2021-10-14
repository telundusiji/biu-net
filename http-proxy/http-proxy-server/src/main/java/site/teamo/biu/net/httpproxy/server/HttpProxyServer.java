package site.teamo.biu.net.httpproxy.server;

import site.teamo.biu.net.common.util.NetworkServer;
import site.teamo.biu.net.httpproxy.server.init.ServerHandlerInitializer;

/**
 * @@author: 爱做梦的锤子
 * @date: 2021/10/13 21:16
 */
public class HttpProxyServer {

    private static int port = 8543;

    public static void main(String[] args) {
        NetworkServer proxyServer = NetworkServer.builder()
                .name("proxy server")
                .port(port)
                .initializer(new ServerHandlerInitializer())
                .buildServer();

        proxyServer.start(true);
    }
}
