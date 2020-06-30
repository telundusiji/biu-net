package site.teamo.biu.net.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.net.web.service.ProxyServerService;
import site.teamo.biu.net.web.util.BiuNetJSONResult;
import site.teamo.site.biu.net.web.pojo.vo.ProxyServerBO;
import site.teamo.site.biu.net.web.pojo.vo.ProxyServerVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Api(value = "代理映射管理", tags = "代理映射管理")
@RestController
@RequestMapping("proxyServer")
public class ProxyServerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyServerController.class);

    @Autowired
    private ProxyServerService proxyServerService;

    @ApiOperation(value = "查询已创建的代理", notes = "查询已创建的代理")
    @GetMapping("/list")
    public BiuNetJSONResult list() {
        List<ProxyServerVO> list = proxyServerService.queryAll();
        return BiuNetJSONResult.ok(list);
    }

    @ApiOperation(value = "添加一个代理映射", notes = "创建一个代理映射")
    @PostMapping("/create")
    public BiuNetJSONResult create(@RequestBody @Validated ProxyServerBO proxyServerBO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> map = bindingResult.getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return BiuNetJSONResult.errorMap(map);
            }
            proxyServerService.create(proxyServerBO);
            return BiuNetJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error("添加代理映射失败", e);
            return BiuNetJSONResult.errorMsg("添加代理映射失败");
        }
    }
}
