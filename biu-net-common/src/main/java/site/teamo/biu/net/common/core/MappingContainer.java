package site.teamo.biu.net.common.core;

import com.alibaba.fastjson.JSON;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.biu.net.common.exception.UnregisteredClientException;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
public class MappingContainer {

    private static Map<ClientInfo.Key, ClientInfo> clientInfoMap = new ConcurrentHashMap<>();

    private static Map<String, ProxyServerInfo> proxyServerInfoMap = new ConcurrentHashMap<>();

    public static void loginClient(ClientInfo clientInfo) throws UnregisteredClientException, IllegalInformationException {
        if (clientInfo == null) {
            throw new IllegalInformationException("client登陆信息是null");
        }
        if (clientInfo.getChannelHandlerContext() == null) {
            throw new IllegalInformationException("client登陆时，ChannelHandlerContext不能为null");
        }
        ClientInfo client = clientInfoMap.get(clientInfo.key());
        if (client == null) {
            throw new UnregisteredClientException(JSON.toJSONString(client.key()));
        }
        client.setChannelHandlerContext(clientInfo.getChannelHandlerContext());
        client.setOnline(YesNo.YES.type);
        client.setLoginTime(new Date());
    }

    public static void registerClient(ClientInfo clientInfo) throws IllegalInformationException {
        if (clientInfo == null) {
            throw new IllegalInformationException("client注册信息是null");
        }
        clientInfo.check();
        if (clientInfoMap.get(clientInfo.getId()) != null) {
            return;
        }
        clientInfoMap.put(clientInfo.key(), clientInfo);
    }

    public static void registerProxyServer(ProxyServerInfo proxyServerInfo) throws IllegalInformationException, UnregisteredClientException {
        if (proxyServerInfo == null) {
            throw new IllegalInformationException("proxyServer注册信息为null");
        }
        ClientInfo client = getClientByClientId(proxyServerInfo.getClientId());
        if (client == null) {
            throw new UnregisteredClientException(proxyServerInfo.getClientId());
        }
        if (proxyServerInfoMap.get(proxyServerInfo.getId()) != null) {
            return;
        }
        proxyServerInfoMap.put(proxyServerInfo.getId(), proxyServerInfo);
    }

    public static ClientInfo getClientByClientName(String name) {
        return clientInfoMap.get(ClientInfo.Key.builder().name(name).build());
    }

    public static ClientInfo getClientByClientId(String id) {
        return clientInfoMap.get(ClientInfo.Key.builder().id(id).build());
    }

    public static ClientInfo getClientByProxyServerPort(String id) {
        ProxyServerInfo info = proxyServerInfoMap.get(id);
        if (info == null) {
            return null;
        }
        return clientInfoMap.get(info.getClientId());
    }

    public static ProxyServerInfo getProxyServer(String id) {
        return proxyServerInfoMap.get(id);
    }

    public static void refresh() {
        clientInfoMap.entrySet().forEach(x -> {
            if (x.getValue().getOnline() == YesNo.YES.type && !x.getValue().getChannelHandlerContext().channel().isOpen()) {
                x.getValue().setOnline(YesNo.NO.type);
                x.getValue().setChannelHandlerContext(null);
            }
        });
    }

}
