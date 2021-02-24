package site.teamo.biu.net.server.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.exception.ResponseCode;
import site.teamo.biu.net.common.info.ClientInfo;
import site.teamo.biu.net.common.info.ProxyInfo;
import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;
import site.teamo.biu.net.common.util.IDFactory;
import site.teamo.biu.net.server.web.dao.ClientMapper;
import site.teamo.biu.net.server.web.dao.ProxyMapper;
import site.teamo.biu.net.server.web.dao.ProxyServerMapper;
import site.teamo.biu.net.server.web.pojo.bo.ProxyBO;
import site.teamo.biu.net.server.web.pojo.vo.ClientVO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyServerVO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyVO;
import site.teamo.biu.net.server.web.service.ProxyService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/14
 */
@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    @Autowired
    private ProxyMapper proxyMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ProxyServerMapper proxyServerMapper;

    @Override
    public PageInfo<ProxyVO> queryAll(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        PageInfo proxyInfoPageInfo = new PageInfo(proxyMapper.selectAll());
        proxyInfoPageInfo.setList(
                ((List<ProxyInfo>) proxyInfoPageInfo.getList()).stream()
                        .map(info -> {
                            try {
                                ProxyVO proxyVO = BiuNetBeanUtil.copyBean(info, ProxyVO.class);
                                ClientInfo clientInfo = clientMapper.selectByPrimaryKey(info.getClientId());
                                ProxyServerInfo proxyServerInfo = proxyServerMapper.selectByPrimaryKey(info.getProxyServerId());
                                proxyVO.setClient(BiuNetBeanUtil.copyBean(clientInfo, ClientVO.class));
                                proxyVO.setProxyServer(BiuNetBeanUtil.copyBean(proxyServerInfo, ProxyServerVO.class));
                                return proxyVO;
                            } catch (Exception e) {
                                log.error("Query all proxy failed for converting into to vo : {} ", JSON.toJSONString(info));
                                throw ResponseCode.BUSINESS.QUERY_ERROR.createRuntimeException("Query all proxy failed", e);
                            }
                        }).collect(Collectors.toList()));
        return proxyInfoPageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProxyInfo create(ProxyBO proxyBO) {
        if (clientMapper.selectByPrimaryKey(proxyBO.getClientId()) == null) {
            throw ResponseCode.BUSINESS.CREATE_ERROR.createRuntimeException("The client id is not exist in DB for creating proxy : " + proxyBO.getClientId());
        }
        if (proxyServerMapper.selectByPrimaryKey(proxyBO.getProxyServerId()) == null) {
            throw ResponseCode.BUSINESS.CREATE_ERROR.createRuntimeException("The proxyServer id is not exist in DB for creating proxy : " + proxyBO.getClientId());
        }
        try {
            ProxyInfo info = BiuNetBeanUtil.copyBean(proxyBO, ProxyInfo.class);
            info.setId(IDFactory.shortId());
            info.setEnable(YesNo.NO.type);
            proxyMapper.insertSelective(info);
            return info;
        } catch (Exception e) {
            log.error("Save proxy info to DB failed for : {} ", JSON.toJSONString(proxyBO));
            throw ResponseCode.BUSINESS.CREATE_ERROR.createRuntimeException("Save proxy info to DB failed", e);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProxyInfo enable(String id) {
        ProxyInfo info = proxyMapper.selectByPrimaryKey(id);
        if (info == null) {
            throw ResponseCode.BUSINESS.UPDATE_ERROR.createRuntimeException("Proxy not exist for enable : " + id);
        }
        ProxyInfo newInfo = ProxyInfo.builder()
                .id(id)
                .enable(YesNo.YES.type)
                .build();

        proxyMapper.updateByPrimaryKeySelective(newInfo);
        return proxyMapper.selectByPrimaryKey(id);
    }
}
