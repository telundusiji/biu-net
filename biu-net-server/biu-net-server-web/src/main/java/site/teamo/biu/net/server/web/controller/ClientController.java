package site.teamo.biu.net.server.web.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.net.common.annoation.Validation;
import site.teamo.biu.net.common.info.ClientInfo;
import site.teamo.biu.net.common.exception.BiuNetRuntimeException;
import site.teamo.biu.net.common.exception.ErrorCode;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;
import site.teamo.biu.net.common.util.BiuNetJSONResult;
import site.teamo.biu.net.server.core.MockClient;
import site.teamo.biu.net.server.core.ServerContext;
import site.teamo.biu.net.server.web.pojo.bo.ClientBO;
import site.teamo.biu.net.server.web.pojo.vo.ClientVO;
import site.teamo.biu.net.server.web.service.ClientService;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
@Api(value = "客户端管理", tags = "客户端管理")
@RestController
@RequestMapping("/client")
@Slf4j
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ServerContext serverContext;

    @ApiOperation(value = "查询所有客户端信息", notes = "查询所有客户端信息")
    @GetMapping("/list")
    public BiuNetJSONResult list() {
        List<ClientVO> clientVOS = clientService.queryAll();
        return BiuNetJSONResult.ok(clientVOS);
    }

    @ApiOperation(value = "创建一个客户端", notes = "创建一个客户端")
    @PostMapping
    @Validation
    public BiuNetJSONResult create(@RequestBody ClientBO clientBO) {
        try {
            ClientInfo info = clientService.create(clientBO);
            serverContext.addClient(BiuNetBeanUtil.copyBean(info, MockClient.Info.class));
            BiuNetBeanUtil.desensitizationData(info);
            return BiuNetJSONResult.ok(info);
        } catch (BiuNetRuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Create client failed in controller for : {} ", JSON.toJSONString(clientBO));
            throw ErrorCode.BUSINESS.CREATE_ERROR.createRuntimeException("Create client failed", e);
        }

    }

}
