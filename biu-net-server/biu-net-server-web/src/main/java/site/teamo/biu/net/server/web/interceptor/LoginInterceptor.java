package site.teamo.biu.net.server.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import site.teamo.biu.net.common.util.BiuNetApplicationUtil;
import site.teamo.biu.net.common.util.BiuNetJSONResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 爱做梦的锤子
 * @create 2021/3/4 16:08
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getRequestURI().startsWith("/api") && !request.getRequestURI().startsWith("/admin/session")) {
            return true;
        }
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader("token");
        }
        if (StringUtils.isEmpty(token)) {
            returnJson(response);
            return false;
        }
        Cache<String, String> userCache = BiuNetApplicationUtil.getBean("userCache", Cache.class);
        String username = userCache.getIfPresent(token);
        if (StringUtils.isEmpty(username)) {
            returnJson(response);
            return false;
        }
        userCache.put(token, username);
        return true;
    }


    private void returnJson(HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            BiuNetJSONResult result = BiuNetJSONResult.error(400, "登录过期");
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.error("Write data to response's writer error", e);
            } else {
                log.warn("Write data to response's writer error");
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
