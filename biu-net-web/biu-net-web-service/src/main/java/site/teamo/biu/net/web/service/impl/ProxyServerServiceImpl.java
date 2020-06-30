package site.teamo.biu.net.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.core.MappingContainer;
import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.biu.net.common.exception.UnregisteredClientException;
import site.teamo.biu.net.server.proxy.BiuNetProxyServer;
import site.teamo.biu.net.web.dao.ClientMapper;
import site.teamo.biu.net.web.dao.ProxyServerMapper;
import site.teamo.biu.net.web.service.ProxyServerService;
import site.teamo.site.biu.net.web.pojo.Client;
import site.teamo.site.biu.net.web.pojo.ProxyServer;
import site.teamo.site.biu.net.web.pojo.vo.ProxyServerBO;
import site.teamo.site.biu.net.web.pojo.vo.ProxyServerVO;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Service
public class ProxyServerServiceImpl implements ProxyServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyServerServiceImpl.class);

    @Autowired
    private ProxyServerMapper proxyServerMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public List<ProxyServerVO> queryAll() {
        List<ProxyServer> proxyServers = proxyServerMapper.selectAll();
        List<ProxyServerVO> proxyServerVOS = proxyServers.stream().map(x -> {
            ProxyServerVO proxyServerVO = ProxyServerVO.builder().build();
            BeanUtils.copyProperties(x, proxyServerVO);
            Client client = clientMapper.selectOne(Client.builder().id(x.getClientId()).build());
            proxyServerVO.setClientName(client == null ? "" : client.getName());
            return proxyServerVO;
        }).collect(Collectors.toList());
        return proxyServerVOS;
    }

    private ExecutorService executorService;

    @Override
    public void create(ProxyServerBO proxyServerBO) throws UnregisteredClientException, IllegalInformationException {
        ProxyServer proxyServer = ProxyServer.builder()
                .id(UUID.randomUUID().toString())
                .clientId(proxyServerBO.getClientId())
                .host(proxyServerBO.getHost())
                .port(proxyServerBO.getPort())
                .targetHost(proxyServerBO.getTargetHost())
                .targetPort(proxyServerBO.getTargetPort())
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        proxyServerMapper.insert(proxyServer);
        ProxyServerInfo proxyServerInfo = ProxyServerInfo.builder()
                .id(proxyServer.getId())
                .clientId(proxyServer.getClientId())
                .host(proxyServer.getHost())
                .port(proxyServer.getPort())
                .targetHost(proxyServer.getTargetHost())
                .targetPort(proxyServer.getTargetPort())
                .build();
        MappingContainer.registerProxyServer(proxyServerInfo);
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
        executorService.submit(() -> {
            try {
                BiuNetProxyServer.open(proxyServerInfo).start();
            } catch (InterruptedException e) {
                LOGGER.info("启动代理服务器失败", e);
            }
        });
    }
}
