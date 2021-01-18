package site.teamo.biu.net.server.web.dao;


import org.springframework.stereotype.Component;
import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.server.web.util.BiuNetMapper;


@Component
public interface ProxyServerMapper extends BiuNetMapper<ProxyServerInfo> {
}