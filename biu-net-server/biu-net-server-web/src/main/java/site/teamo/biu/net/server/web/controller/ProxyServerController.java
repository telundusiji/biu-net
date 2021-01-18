package site.teamo.biu.net.server.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.net.common.annoation.Validation;
import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.common.util.BiuNetJSONResult;
import site.teamo.biu.net.server.core.ServerContext;
import site.teamo.biu.net.server.web.pojo.bo.ProxyServerBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyServerVO;
import site.teamo.biu.net.server.web.service.ProxyServerService;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Api(value = "代理服务端管理", tags = "代理服务端管理")
@RestController
@RequestMapping("proxyServer")
@Slf4j
public class ProxyServerController {

    @Autowired
    private ProxyServerService proxyServerService;

    @Autowired
    private ServerContext serverContext;

    @ApiOperation(value = "查询已创建的代理服务端", notes = "查询已创建的代理服务端")
    @GetMapping("/list")
    public BiuNetJSONResult list() {
        List<ProxyServerVO> list = proxyServerService.queryAll();
        return BiuNetJSONResult.ok(list);
    }

    @ApiOperation(value = "添加一个代理映射", notes = "创建一个代理映射")
    @PostMapping("/create")
    @Validation
    public BiuNetJSONResult create(@RequestBody ProxyServerBO proxyServerBO) {
        ProxyServerInfo info = proxyServerService.create(proxyServerBO);
        serverContext.addProxyServer(info);
        return BiuNetJSONResult.ok(info);
    }
}
