package site.teamo.biu.net.client.core;

import site.teamo.biu.net.common.util.BiuNetApplicationUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/18
 */
public class ClientContext {


    public static ClientContext getInstance() {
        return BiuNetApplicationUtil.getBean(ClientContext.class);
    }

    private Client client;
    private Map<String, ProxyClient> proxyClientMap;

    public ClientContext(Client.Info info) {
        client = info.buildClient();
        client.start();
        proxyClientMap = new ConcurrentHashMap<>();
    }

    public Client getClient() {
        return client;
    }

    public ProxyClient getProxyClient(ProxyClient.Info info) {
        ProxyClient proxyClient = proxyClientMap.get(info.getProxyCtxId());
        if (proxyClient == null) {
            proxyClient = info.buildClient();
            proxyClientMap.put(proxyClient.getInfo().getProxyCtxId(), proxyClient);
            proxyClient.start();
        }
        if (!proxyClient.getNetworkClient().isStarted()) {
            proxyClient.start();
        }
        return proxyClient;
    }

    public ProxyClient getProxyClient(String proxyCtxId) {
        return proxyClientMap.get(proxyCtxId);
    }

    public ProxyClient getAndRemoveProxyClient(String proxyCtxId) {
        return proxyClientMap.remove(proxyCtxId);
    }

}
