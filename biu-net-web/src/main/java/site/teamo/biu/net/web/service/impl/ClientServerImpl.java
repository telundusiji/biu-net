package site.teamo.biu.net.web.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.teamo.biu.net.common.bean.ClientInfo;
import site.teamo.biu.net.common.core.MappingContainer;
import site.teamo.biu.net.common.exception.IllegalInformationException;
import site.teamo.biu.net.common.util.MD5Util;
import site.teamo.biu.net.web.bean.Client;
import site.teamo.biu.net.web.bean.vo.ClientBO;
import site.teamo.biu.net.web.bean.vo.ClientVO;
import site.teamo.biu.net.web.dao.ClientMapper;
import site.teamo.biu.net.web.service.ClientService;
import sun.security.provider.MD5;
import tk.mybatis.mapper.entity.Example;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
@Service
public class ClientServerImpl implements ClientService {

    @Autowired
    private ClientMapper clientMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryClientNameIsExist(String clientName) {
        Example example = new Example(Client.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", clientName);
        Client client = clientMapper.selectOneByExample(example);
        return client != null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ClientVO> queryAll() {
        List<ClientVO> list = clientMapper.selectAll().stream().map(x -> {
            ClientVO clientVO = ClientVO.builder().build();
            BeanUtils.copyProperties(x, clientVO);
            return clientVO;
        }).collect(Collectors.toList());
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void create(ClientBO clientBO) throws NoSuchAlgorithmException, IllegalInformationException {
        Client client = Client.builder()
                .id(UUID.randomUUID().toString())
                .name(clientBO.getClientName())
                .password(MD5Util.toMD5(clientBO.getPassword()))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        clientMapper.insert(client);
        client = clientMapper.selectOne(client);
        MappingContainer.registerClient(ClientInfo.builder()
                .id(client.getId())
                .name(client.getName())
                .password(client.getPassword())
                .build());
    }

}