package site.teamo.biu.net.server.web.service;

import com.github.pagehelper.PageInfo;
import site.teamo.biu.net.common.info.ClientInfo;
import site.teamo.biu.net.server.web.pojo.bo.ClientBO;
import site.teamo.biu.net.server.web.pojo.vo.ClientVO;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public interface ClientService {

    /**
     * 查询所有客户端信息
     *
     * @return
     */
    PageInfo<ClientVO> queryAll(int pageNo, int pageSize);

    /**
     * 创建客户端
     */
    ClientInfo create(ClientBO clientBO) throws Exception;
}
