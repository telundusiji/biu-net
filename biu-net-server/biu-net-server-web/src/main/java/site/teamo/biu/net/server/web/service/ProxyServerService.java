package site.teamo.biu.net.server.web.service;

import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.server.web.pojo.bo.ProxyServerBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyServerVO;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
public interface ProxyServerService {

    List<ProxyServerVO> queryAll();

    ProxyServerInfo create(ProxyServerBO proxyServerBO);
}
