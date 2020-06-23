package site.teamo.biu.net.common.core;

import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.biu.net.common.exception.UnregisteredClientException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
public class MappingContainer {

    private static Map<String, ClientInfo> clientInfoMap = new ConcurrentHashMap<>();

    private static Map<Integer, ProxyServerInfo> proxyServerInfoMap = new ConcurrentHashMap<>();

    public static void loginClient(ClientInfo clientInfo) throws UnregisteredClientException, IllegalInformationException {
        if (clientInfo == null) {
            throw new IllegalInformationException("client登陆信息是null");
        }
        if (clientInfo.getChannelHandlerContext() == null) {
            throw new IllegalInformationException("client登陆时，ChannelHandlerContext不能为null");
        }
        ClientInfo client = clientInfoMap.get(clientInfo.getKey());
        if (client == null) {
            throw new UnregisteredClientException(client.getKey());
        }
        client.setChannelHandlerContext(clientInfo.getChannelHandlerContext());
        client.setOnline(YesNo.YES.type);
    }

    public static void registerClient(ClientInfo clientInfo) throws IllegalInformationException {
        if (clientInfo == null) {
            throw new IllegalInformationException("client注册信息是null");
        }
        clientInfo.check();
        if (clientInfoMap.get(clientInfo.getKey()) != null) {
            return;
        }
        clientInfoMap.put(clientInfo.getKey(), clientInfo);
    }

    public static void registerProxyServer(ProxyServerInfo proxyServerInfo) throws IllegalInformationException, UnregisteredClientException {
        if (proxyServerInfo == null) {
            throw new IllegalInformationException("proxyServer注册信息为null");
        }
        ClientInfo client = clientInfoMap.get(proxyServerInfo.getClientKey());
        if (client == null) {
            throw new UnregisteredClientException(proxyServerInfo.getClientKey());
        }
        ProxyServerInfo proxyServer = proxyServerInfoMap.get(proxyServerInfo.getPort());
        if (proxyServer != null) {
            return;
        }
        proxyServerInfoMap.put(proxyServerInfo.getPort(),proxyServerInfo);
    }

    public static ClientInfo getClientByClientKey(String key){
        return clientInfoMap.get(key);
    }

    public static ClientInfo getClientByProxyServerPort(int port){
        ProxyServerInfo info = proxyServerInfoMap.get(port);
        if(info==null){
            return null;
        }
        return clientInfoMap.get(info.getClientKey());
    }

    public static ProxyServerInfo getProxyServer(int port){
        return proxyServerInfoMap.get(port);
    }
}
