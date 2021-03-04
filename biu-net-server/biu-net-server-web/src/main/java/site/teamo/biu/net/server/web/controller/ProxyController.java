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
import site.teamo.biu.net.common.info.ProxyInfo;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;
import site.teamo.biu.net.common.util.BiuNetJSONResult;
import site.teamo.biu.net.server.core.ServerContext;
import site.teamo.biu.net.server.web.pojo.bo.ProxyBO;
import site.teamo.biu.net.server.web.pojo.vo.ProxyVO;
import site.teamo.biu.net.server.web.service.ProxyService;

import javax.validation.constraints.Min;

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
    @Validation
    public BiuNetJSONResult list(@RequestParam(defaultValue = "1", required = false)
                                 @Min(value = 1, message = "PageNo must be greater than or equal to 1")
                                 @ApiParam(value = "页码", example = "1") Integer pageNo,
                                 @RequestParam(defaultValue = "10", required = false)
                                 @Range(min = 5, max = 1000, message = "PageSize must be greater than or equal to 5 and less than or equal to 1000")
                                 @ApiParam(value = "页面大小", example = "10") Integer pageSize) {
        PageInfo<ProxyVO> proxyVOPageInfo = proxyService.queryAll(pageNo, pageSize);
        return BiuNetJSONResult.ok(proxyVOPageInfo);
    }

    @ApiOperation(value = "添加一个代理", notes = "添加一个代理")
    @PostMapping("/create")
    @Validation
    public BiuNetJSONResult create(@RequestBody ProxyBO proxyBO) {
        ProxyInfo info = proxyService.create(proxyBO);
        serverContext.addProxy(info);
        return BiuNetJSONResult.ok(BiuNetBeanUtil.copyBean(info, new ProxyVO()));
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
