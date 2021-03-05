package site.teamo.biu.net.server.web.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.net.common.annoation.Validation;
import site.teamo.biu.net.common.info.ProxyServerInfo;
import site.teamo.biu.net.common.util.BiuNetJSONResult;
import site.teamo.biu.net.server.core.ServerContext;
import site.teamo.biu.net.server.web.pojo.bo.ProxyServerBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyServerVO;
import site.teamo.biu.net.server.web.service.ProxyServerService;

import javax.validation.constraints.Min;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Api(value = "代理服务端管理", tags = "代理服务端管理")
@RestController
@RequestMapping("/api/proxyServer")
@Slf4j
public class ProxyServerController {

    @Autowired
    private ProxyServerService proxyServerService;

    @Autowired
    private ServerContext serverContext;

    @ApiOperation(value = "查询已创建的代理服务端", notes = "查询已创建的代理服务端")
    @GetMapping("/list")
    public BiuNetJSONResult list(@RequestParam(defaultValue = "1", required = false)
                                 @Min(value = 1, message = "PageNo must be greater than or equal to 1")
                                 @ApiParam(value = "页码", example = "1")
                                         Integer pageNo,
                                 @RequestParam(defaultValue = "10", required = false)
                                 @Range(min = 5, max = 1000, message = "PageSize must be greater than or equal to 5 and less than or equal to 1000")
                                 @ApiParam(value = "页面大小", example = "10")
                                         Integer pageSize) {
        PageInfo<ProxyServerVO> proxyServerVOPageInfo = proxyServerService.queryAll(pageNo, pageSize);
        return BiuNetJSONResult.ok(proxyServerVOPageInfo);
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
