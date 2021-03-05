package site.teamo.biu.net.server.web.controller;

import com.google.common.cache.Cache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import site.teamo.biu.net.common.exception.ResponseCode;
import site.teamo.biu.net.common.util.BiuNetJSONResult;
import site.teamo.biu.net.common.util.MD5Util;
import site.teamo.biu.net.server.web.pojo.bo.AdminLoginBO;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author 爱做梦的锤子
 * @create 2021/3/4 15:52
 */
@Api("管理员")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Value("${biu-net.admin.username}")
    private String username;

    @Value("${biu-net.admin.password}")
    private String password;

    @Autowired
    private Cache<String, String> userCache;

    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public BiuNetJSONResult login(
            @ApiParam("登录信息")
            @RequestBody AdminLoginBO adminLoginBO) throws NoSuchAlgorithmException {
        if (StringUtils.equals(username, adminLoginBO.getUsername())
                && StringUtils.equals(MD5Util.toMD5(adminLoginBO.getPassword()), password)) {
            String token = UUID.randomUUID().toString();
            userCache.put(token, username);
            return BiuNetJSONResult.okMap().put("token", token).build();
        }
        return BiuNetJSONResult.error(ResponseCode.PARAMETER.BAD_PARAMETER.code, "用户名或密码错误");
    }


    @ApiOperation("管理员登出")
    @PostMapping("/logout")
    public BiuNetJSONResult logout(@RequestParam String token) {
        userCache.invalidate(token);
        return BiuNetJSONResult.ok();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/session")
    public BiuNetJSONResult session() {
        return BiuNetJSONResult.okMap().put("username", username).build();
    }

}
