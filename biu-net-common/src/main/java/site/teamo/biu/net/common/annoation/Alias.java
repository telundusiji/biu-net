package site.teamo.biu.net.common.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/27
 */

/**
 *
 * 字段别名注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {

    /**
     * 别名
     * @return
     */
    String[] value() default "";

}
