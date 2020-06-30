package site.teamo.biu.net.web.service;

import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.biu.net.common.exception.UnregisteredClientException;
import site.teamo.site.biu.net.web.pojo.vo.ProxyServerBO;
import site.teamo.site.biu.net.web.pojo.vo.ProxyServerVO;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
public interface ProxyServerService {

    List<ProxyServerVO> queryAll();

    void create(ProxyServerBO proxyServerBO) throws UnregisteredClientException, IllegalInformationException;
}
