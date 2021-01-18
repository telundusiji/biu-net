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
 * 敏感信息字段表示注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    /**
     * 替代函数的类型
     *
     * @return
     */
    Class<? extends Replacer> value() default NullReplacer.class;

    @FunctionalInterface
    interface Replacer<T> {
        T replace(T source);
    }

    class NullReplacer implements Replacer<Object> {
        @Override
        public Object replace(Object source) {
            return null;
        }
    }

    class StringReplacer implements Replacer<String> {

        @Override
        public String replace(String source) {
            return "****";
        }
    }
}
