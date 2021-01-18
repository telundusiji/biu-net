package site.teamo.biu.net.server.web.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.teamo.biu.net.common.info.ProxyInfo;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.exception.ErrorCode;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;
import site.teamo.biu.net.server.web.dao.ClientMapper;
import site.teamo.biu.net.server.web.dao.ProxyMapper;
import site.teamo.biu.net.server.web.dao.ProxyServerMapper;
import site.teamo.biu.net.server.web.pojo.bo.ProxyBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyVO;
import site.teamo.biu.net.server.web.service.ProxyService;

import java.util.List;
import java.util.UUID;
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
    public List<ProxyVO> queryAll() {
        return proxyMapper.selectAll().stream()
                .map(info -> {
                    try {
                        return BiuNetBeanUtil.copyBean(info, ProxyVO.class);
                    } catch (Exception e) {
                        log.error("Query all proxy failed for converting into to vo : {} ", JSON.toJSONString(info));
                        throw ErrorCode.BUSINESS.QUERY_ERROR.createRuntimeException("Query all proxy failed", e);
                    }
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProxyInfo create(ProxyBO proxyBO) {
        if (clientMapper.selectByPrimaryKey(proxyBO.getClientId()) == null) {
            throw ErrorCode.BUSINESS.CREATE_ERROR.createRuntimeException("The client id is not exist in DB for creating proxy : " + proxyBO.getClientId());
        }
        if (proxyServerMapper.selectByPrimaryKey(proxyBO.getProxyServerId()) == null) {
            throw ErrorCode.BUSINESS.CREATE_ERROR.createRuntimeException("The proxyServer id is not exist in DB for creating proxy : " + proxyBO.getClientId());
        }
        try {
            ProxyInfo info = BiuNetBeanUtil.copyBean(proxyBO, ProxyInfo.class);
            info.setId(UUID.randomUUID().toString());
            proxyMapper.insertSelective(info);
            return info;
        } catch (Exception e) {
            log.error("Save proxy info to DB failed for : {} ", JSON.toJSONString(proxyBO));
            throw ErrorCode.BUSINESS.CREATE_ERROR.createRuntimeException("Save proxy info to DB failed", e);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProxyInfo enable(String id) {
        ProxyInfo info = proxyMapper.selectByPrimaryKey(id);
        if (info == null) {
            throw ErrorCode.BUSINESS.UPDATE_ERROR.createRuntimeException("Proxy not exist for enable : " + id);
        }
        ProxyInfo newInfo = ProxyInfo.builder()
                .id(id)
                .enable(YesNo.YES.type)
                .build();

        proxyMapper.updateByPrimaryKeySelective(newInfo);
        return proxyMapper.selectByPrimaryKey(id);
    }
}
