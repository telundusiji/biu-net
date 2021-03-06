package site.teamo.biu.net.server.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.teamo.biu.net.common.exception.ResponseCode;
import site.teamo.biu.net.common.info.ClientInfo;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;
import site.teamo.biu.net.common.util.IDFactory;
import site.teamo.biu.net.common.util.MD5Util;
import site.teamo.biu.net.common.util.RSAUtil;
import site.teamo.biu.net.server.web.dao.ClientMapper;
import site.teamo.biu.net.server.web.pojo.bo.ClientBO;
import site.teamo.biu.net.server.web.pojo.vo.ClientVO;
import site.teamo.biu.net.server.web.service.ClientService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
@Service
@Slf4j
public class ClientServerImpl implements ClientService {

    @Autowired
    private ClientMapper clientMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PageInfo<ClientVO> queryAll(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        PageInfo clientInfoPageInfo = new PageInfo(clientMapper.selectAll());
        clientInfoPageInfo.setList(
                ((List<ClientInfo>) clientInfoPageInfo.getList()).stream().map(info -> {
                    try {
                        return BiuNetBeanUtil.copyBean(info, ClientVO.class);
                    } catch (Exception e) {
                        log.error("Query all client failed for converting client info to client vo : {}", JSON.toJSONString(info));
                        throw ResponseCode.BUSINESS.QUERY_ERROR.createRuntimeException("Query all client failed", e);
                    }
                }).collect(Collectors.toList())
        );
        return clientInfoPageInfo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ClientInfo create(ClientBO clientBO) {
        try {
            ClientInfo info = BiuNetBeanUtil.copyBean(clientBO, ClientInfo.class);
            info.setId(IDFactory.shortId());
            info.setPassword(MD5Util.toMD5(clientBO.getPassword()));
            RSAUtil.RSAKey rsaKey = RSAUtil.generateRSAKey();
            info.setPrivateKey(rsaKey.getPrivateKey()).setPublicKey(rsaKey.getPublicKey());
            clientMapper.insert(info);
            return info;
        } catch (Exception e) {
            log.error("Save client info to DB failed for : {} ", JSON.toJSONString(clientBO));
            throw ResponseCode.BUSINESS.CREATE_ERROR.createRuntimeException("Save client info to DB failed", e);
        }

    }

}
