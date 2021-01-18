package site.teamo.biu.net.server.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.net.common.annoation.Validation;
import site.teamo.biu.net.common.info.ProxyInfo;
import site.teamo.biu.net.common.util.BiuNetJSONResult;
import site.teamo.biu.net.server.core.ServerContext;
import site.teamo.biu.net.server.web.pojo.bo.ProxyBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyVO;
import site.teamo.biu.net.server.web.service.ProxyService;

import java.util.List;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Api(value = "代理管理", tags = "代理管理")
@RestController
@RequestMapping("proxy")
@Slf4j
public class ProxyController {

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private ServerContext serverContext;

    @ApiOperation(value = "查询已创建的代理", notes = "查询已创建的代理")
    @GetMapping("/list")
    public BiuNetJSONResult list() {
        List<ProxyVO> list = proxyService.queryAll();
        return BiuNetJSONResult.ok(list);
    }

    @ApiOperation(value = "添加一个代理", notes = "添加一个代理")
    @PostMapping("/create")
    @Validation
    public BiuNetJSONResult create(@RequestBody ProxyBO proxyBO) {
        ProxyInfo info = proxyService.create(proxyBO);
        serverContext.addProxy(info);
        return BiuNetJSONResult.ok(info);
    }

    @ApiOperation(value = "启用一个代理", notes = "启用一个代理")
    @PostMapping("/enable/{id}")
    @Validation
    public BiuNetJSONResult enable(@PathVariable String id) {
        ProxyInfo info = proxyService.enable(id);
        serverContext.enableProxy(id);
        return BiuNetJSONResult.ok(info);
    }
}
