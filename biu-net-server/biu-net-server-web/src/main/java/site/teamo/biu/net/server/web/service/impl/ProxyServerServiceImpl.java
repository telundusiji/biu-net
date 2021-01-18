package site.teamo.biu.net.server.web.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.common.exception.ErrorCode;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;
import site.teamo.biu.net.server.web.dao.ProxyServerMapper;
import site.teamo.biu.net.server.web.pojo.bo.ProxyServerBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyServerVO;
import site.teamo.biu.net.server.web.service.ProxyServerService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Service
@Slf4j
public class ProxyServerServiceImpl implements ProxyServerService {

    @Autowired
    private ProxyServerMapper proxyServerMapper;

    @Override
    public List<ProxyServerVO> queryAll() {
        List<ProxyServerVO> proxyServerVOS = proxyServerMapper.selectAll().stream().map(info -> {
            try {
                return BiuNetBeanUtil.copyBean(info, ProxyServerVO.class);
            } catch (Exception e) {
                log.error("Query all proxyServer failed for converting info to vo : {} ", JSON.toJSONString(info));
                throw ErrorCode.BUSINESS.QUERY_ERROR.createRuntimeException("Query all proxyServer failed", e);
            }
        }).collect(Collectors.toList());
        return proxyServerVOS;
    }

    private ExecutorService executorService;

    @Override
    public ProxyServerInfo create(ProxyServerBO proxyServerBO) {
        try {
            ProxyServerInfo info = BiuNetBeanUtil.copyBean(proxyServerBO, ProxyServerInfo.class);
            info.setId(UUID.randomUUID().toString());
            proxyServerMapper.insertSelective(info);
            return info;
        } catch (Exception e) {
            log.error("Save proxyServer info to DB failed for : {} ", JSON.toJSONString(proxyServerBO));
            throw ErrorCode.BUSINESS.CREATE_ERROR.createRuntimeException("Save proxyServer info to DB failed", e);
        }
    }
}
