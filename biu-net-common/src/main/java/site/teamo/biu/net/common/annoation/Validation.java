package site.teamo.biu.net.common.annoation;

import java.lang.annotation.*;


/**
 * @author 爱做梦的锤子
 * @create 2021/1/12
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Validation {
}
