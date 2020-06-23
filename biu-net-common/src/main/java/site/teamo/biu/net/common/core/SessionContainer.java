package site.teamo.biu.net.common.core;

import io.netty.channel.ChannelHandlerContext;
import site.teamo.biu.net.common.bean.SessionInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
public class SessionContainer {
    public static Map<String, SessionInfo> sessionInfoMap = new ConcurrentHashMap<>();

    public static void put(SessionInfo sessionInfo) {
        sessionInfoMap.put(sessionInfo.getId(),sessionInfo);
    }

    public static SessionInfo put(ChannelHandlerContext context){
        SessionInfo session = SessionInfo.builder().channelHandlerContext(context).build();
        sessionInfoMap.put(session.getId(),session);
        return session;
    }

    public static SessionInfo get(String id){
        return sessionInfoMap.get(id);
    }
}
