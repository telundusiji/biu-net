package site.teamo.biu.net.server.web.service;

import site.teamo.biu.net.common.info.ProxyInfo;
import site.teamo.biu.net.server.web.pojo.bo.ProxyBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyVO;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/14
 */
public interface ProxyService {
    List<ProxyVO> queryAll();

    ProxyInfo create(ProxyBO proxyBO);

    ProxyInfo enable(String id);
}
