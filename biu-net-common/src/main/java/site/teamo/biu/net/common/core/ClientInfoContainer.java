package site.teamo.biu.net.common.core;

import io.netty.util.AttributeKey;
import site.teamo.biu.net.common.bean.ClientInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class ClientInfoContainer {

    private static Map<String, ClientInfo> clientInfoMap = new ConcurrentHashMap<>();

    public static void register(ClientInfo clientInfo){
        clientInfoMap.put(clientInfo.getKey(),clientInfo);
    }

    public static ClientInfo get(String key){
        return clientInfoMap.get(key);
    }

}
