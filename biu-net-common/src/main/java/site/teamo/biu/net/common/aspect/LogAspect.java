package site.teamo.biu.net.common.aspect;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.StringJoiner;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Value("${biu-net.project.debug:false}")
    private boolean debug;

    @AfterReturning(value = "@annotation(io.swagger.annotations.ApiOperation)", returning = "response")
    public void apiOperationLog(JoinPoint point, Object response) {
        if (!debug) {
            return;
        }
        Object[] args = point.getArgs();
        Method method = MethodSignature.class.cast(point.getSignature()).getMethod();
        StringJoiner requestLogJoiner = new StringJoiner(", ");
        if (args != null && args.length > 0) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < args.length; i++) {
                if (parameters[i].getAnnotation(ApiParam.class) != null) {
                    requestLogJoiner.add(String.format("%s[%s]:%s",
                            parameters[i].getName(),
                            args[i].getClass().getSimpleName(),
                            JSON.toJSONString(args[i])));
                }
            }
        }
        log.info("API[{}] \n Request: {} \n Response: {}", point.getSignature().toShortString(), requestLogJoiner.toString(), JSON.toJSONString(response));
    }

}
