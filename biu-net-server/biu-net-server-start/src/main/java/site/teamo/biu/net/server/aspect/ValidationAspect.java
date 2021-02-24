package site.teamo.biu.net.server.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.teamo.biu.net.common.exception.ResponseCode;
import site.teamo.biu.net.common.util.BiuNetJSONResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/13
 */
@Component
@Aspect
@Slf4j
@Order(1)
public class ValidationAspect {

    /**
     * 创建一个校验工具
     */
    private static ValidatorImpl validator = ValidatorImpl.class.cast(Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(false)
            .buildValidatorFactory()
            .getValidator());

    @Around("@annotation(site.teamo.biu.net.common.annoation.Validation)")
    public Object validation(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Method method = MethodSignature.class.cast(pjp.getSignature()).getMethod();
        /**
         * 先校验方法入参
         */
        Set<ConstraintViolation<Object>> constraintViolations = validator.validateParameters(pjp.getTarget(), method, args);
        if (constraintViolations.size() > 0) {
            return error(constraintViolations, pjp);
        }
        //再循环遍历所有参数，进行参数校验
        for (Object parameter : args) {
            //对参数进行校验
            constraintViolations = validator.validate(parameter);
            //封装错误信息
            if (constraintViolations.size() > 0) {
                return error(constraintViolations, pjp);
            }
        }
        // 目标方法执行
        return pjp.proceed();
    }

    private BiuNetJSONResult error(Set<ConstraintViolation<Object>> constraintViolations, ProceedingJoinPoint pjp) {
        Map<String, String> result = constraintViolations.stream()
                .collect(Collectors.toMap(cv -> cv.getPropertyPath().toString(), cv -> cv.getInvalidValue() + " [" + cv.getMessage() + "]"));
        //启动若为debug模式则打印详情
        if (log.isDebugEnabled()) {
            result.forEach((key, value) -> log.error("validation[{}.{}]=>{}:{}",
                    pjp.getTarget().getClass().getName(),
                    pjp.getSignature().getName(),
                    key,
                    value
            ));
        }
        return BiuNetJSONResult.error(ResponseCode.PARAMETER.BAD_PARAMETER.code, result);
    }
}
