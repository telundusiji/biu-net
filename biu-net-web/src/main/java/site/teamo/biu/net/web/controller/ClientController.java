package site.teamo.biu.net.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.net.web.bean.vo.ClientBO;
import site.teamo.biu.net.web.bean.vo.ClientVO;
import site.teamo.biu.net.web.service.ClientService;
import site.teamo.biu.net.web.util.BiuNetJSONResult;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
@Api(value = "客户端管理", tags = "客户端管理")
@RestController
@RequestMapping("/client")
public class ClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @ApiOperation(value = "校验客户端名称是否存在", notes = "校验客户端名称是否存在")
    @GetMapping("/checkClientNameIsExists")
    public BiuNetJSONResult checkClientNameIsExists(@ApiParam(value = "客户端名称", name = "clientName") @RequestParam String clientName) {
        if (StringUtils.isBlank(clientName)) {
            return BiuNetJSONResult.errorMsg("客户端名称不能为空");
        }
        if (clientService.queryClientNameIsExist(clientName)) {
            return BiuNetJSONResult.errorMsg("客户端名称已经存在");
        }
        return BiuNetJSONResult.ok();
    }

    @ApiOperation(value = "查询所有客户端信息", notes = "查询所有客户端信息")
    @GetMapping("/list")
    public BiuNetJSONResult list() {
        List<ClientVO> clientVOS = clientService.queryAll();
        return BiuNetJSONResult.ok(clientVOS);
    }

    @ApiOperation(value = "创建一个客户端", notes = "创建一个客户端")
    @PostMapping
    public BiuNetJSONResult create(@RequestBody @Valid ClientBO clientBO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> map = bindingResult.getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return BiuNetJSONResult.errorMap(map);
            }
            clientService.create(clientBO);
            return BiuNetJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error("注册客户端错误", e);
            return BiuNetJSONResult.errorMsg("添加客户端失败");
        }
    }

}
