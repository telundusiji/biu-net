package site.teamo.biu.net.server.core;

import io.netty.channel.ChannelHandlerContext;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.info.ClientInfo;
import site.teamo.biu.net.common.info.ProxyInfo;
import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.common.util.BiuNetApplicationUtil;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
public class ServerContext {
    public static ServerContext getInstance() {
        return BiuNetApplicationUtil.getBean(ServerContext.class);
    }

    private Map<String, MockClient> clients = new ConcurrentHashMap<>();
    private Map<String, ProxyServer> proxyServers = new ConcurrentHashMap<>();
    private Map<Object, Proxy> proxies = new ConcurrentHashMap<>();
    private PublicServer publicServer;

    /**
     * 初始化ProxyContext
     *
     * @param clientInfos      已存在的客户端信息
     * @param proxyServerInfos 已存在的代理服务端信息
     */
    public ServerContext(List<ClientInfo> clientInfos, List<ProxyServerInfo> proxyServerInfos) {
        /**
         * 将ClientInfo转换为MockClient
         */
        clients.putAll(
                clientInfos.stream()
                        .map(info -> {
                            MockClient.Info mc = MockClient.Info.builder().build();
                            BiuNetBeanUtil.copyBean(info, mc);
                            return mc.buildClient();
                        })
                        .collect(Collectors.toMap(client -> client.getInfo().getId(), client -> client))
        );

        /**
         * 将ProxyServerInfo转换为ProxyServer
         */
        proxyServers.putAll(
                proxyServerInfos.stream()
                        .map(info -> {
                            ProxyServer.Info proxyServerInfo = ProxyServer.Info.builder().build();
                            BiuNetBeanUtil.copyBean(info, proxyServerInfo);
                            return proxyServerInfo;
                        })
                        .map(info -> info.buildProxyServer())
                        .collect(Collectors.toMap(proxyServer -> proxyServer.getInfo().getId(), proxyServer -> proxyServer))
        );
    }

    public void startPublicServer(int port) {
        if (publicServer == null) {
            publicServer = PublicServer.Info.builder()
                    .port(port)
                    .build().buildPublicServer();
        }
        publicServer.start();
    }

    public void addProxies(List<ProxyInfo> proxyInfos) {
        proxyInfos.stream().forEach(info -> addProxy(info));
    }

    public void addProxy(ProxyInfo info) {
        MockClient mockClient = clients.get(info.getClientId());
        ProxyServer proxyServer = proxyServers.get(info.getProxyServerId());
        Proxy proxy = new Proxy()
                .setInfo(Proxy.Info.create(info))
                .setMockClient(mockClient)
                .setProxyServer(proxyServer);
        proxies.put(proxy.getInfo().getId(), proxy);
        proxies.put(proxy.getInfo().getClientId(), proxy);
        proxies.put(proxy.getInfo().getProxyServerId(), proxy);
        proxies.put(proxy.getProxyServer().getInfo().getPort(), proxy);
        if (YesNo.YES.type == proxy.getInfo().getEnable()) {
            proxy.getProxyServer().start();
        }
    }

    public void enableProxy(String id) {
        Proxy proxy = proxies.get(id);
        proxy.start();
    }

    public void addClient(MockClient.Info info) {
        clients.put(info.getId(), info.buildClient());
    }

    public void addProxyServer(ProxyServerInfo info) {
        ProxyServer.Info pInfo = ProxyServer.Info.builder().build();
        BiuNetBeanUtil.copyBean(info, pInfo);
        proxyServers.put(info.getId(), pInfo.buildProxyServer());
    }

    public Proxy getProxy(Object key) {
        return proxies.get(key);
    }

    public void onLine(String clientId, ChannelHandlerContext ctx) {
        MockClient mockClient = clients.get(clientId);
        mockClient.getInfo().setOnline(YesNo.YES.type);
        mockClient.setCtx(ctx);
        clients.put(ctx.channel().id().asLongText(), mockClient);
    }

    public void offLine(String key) {
        MockClient mockClient = clients.get(key);
        mockClient.getInfo().setOnline(YesNo.NO.type);
        clients.remove(mockClient.getCtx().channel().id().asLongText());
        mockClient.getCtx().disconnect();
        mockClient.getCtx().close();
        mockClient.setCtx(null);
    }

    public MockClient getClient(String key) {
        return clients.get(key);
    }
}
