package site.teamo.biu.net.server.web.service;

import com.github.pagehelper.PageInfo;
import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.server.web.pojo.bo.ProxyServerBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyServerVO;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
public interface ProxyServerService {

    PageInfo<ProxyServerVO> queryAll(int pageNo, int pageSize);

    ProxyServerInfo create(ProxyServerBO proxyServerBO);
}
