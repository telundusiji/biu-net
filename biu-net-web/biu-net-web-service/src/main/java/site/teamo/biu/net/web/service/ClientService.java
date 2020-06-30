package site.teamo.biu.net.web.service;

import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.site.biu.net.web.pojo.vo.ClientBO;
import site.teamo.site.biu.net.web.pojo.vo.ClientVO;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public interface ClientService {

    void loadClient() throws IllegalInformationException;

    /**
     * 查询客户端名称是否存在
     * @param clientName
     * @return
     */
    boolean queryClientNameIsExist(String clientName);

    /**
     * 查询所有客户端信息
     * @return
     */
    List<ClientVO> queryAll();

    /**
     * 创建客户端
     */
    void create(ClientBO clientBO) throws Exception;
}
