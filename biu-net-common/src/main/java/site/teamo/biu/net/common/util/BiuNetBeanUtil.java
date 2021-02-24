package site.teamo.biu.net.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.annoation.Alias;
import site.teamo.biu.net.common.annoation.Sensitive;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/17
 */

/**
 * map类型数据与JavaObject相互转换
 */
@Slf4j
public class BiuNetBeanUtil {

    /**
     * map类型转换 JavaObject，转换失败时返回null或者抛出异常
     *
     * @param map    要转换的数据
     * @param tClass 转换结果的Java类型
     * @param <T>    Java类型泛型
     * @param <V>    转换map的value泛型
     * @return 转换结果
     * @throws Exception 转换失败时抛出异常
     */
    public static <T, V> T map2Object(Map<String, V> map, Class<T> tClass) throws Exception {
        if (map == null || map.isEmpty() || tClass == null) {
            return null;
        }
        T t = tClass.newInstance();

        Map<String, Field> fieldMap = aliasFieldMap(tClass);

        map.forEach((key, value) -> {
            try {
                Field field = fieldMap.get(key);
                if (field == null) {
                    return;
                }
                field.setAccessible(true);
                field.set(t, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return t;
    }

    /**
     * map类型转换 JavaObject并使用Optional封装，转换失败时返回Optional.empty
     *
     * @param map    要转换的数据
     * @param tClass 转换结果的Java类型
     * @param <T>    Java类型泛型
     * @param <V>    转换map的value泛型
     * @return 转换结果
     */
    public static <T, V> Optional<T> map2ObjectOptional(Map<String, V> map, Class<T> tClass) {
        try {
            return Optional.ofNullable(map2Object(map, tClass));
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("Exception converting map to entity", e);
            } else {
                log.warn("Exception converting map to entity:{}", e.getMessage());
            }
            return Optional.empty();
        }
    }

    /**
     * 将JavaObject中数据存储到map中
     *
     * @param o   需要抽出数据的object
     * @param <V> 转换结果的map的value的泛型
     * @return 转换后的map
     */
    public static <V> Map<String, V> object2Map(Object o) {
        return object2Map(o, false);
    }

    /**
     * 将JavaObject中数据存储到map中
     *
     * @param o         需要抽出数据的object
     * @param keepNulls JavaObject中值为空的成员是否保留
     * @param <V>       转换结果的map的value的泛型
     * @return 转换后的map
     */
    public static <V> Map<String, V> object2Map(Object o, boolean keepNulls) {
        Map<String, V> result = new HashMap<>();
        if (o == null) {
            return result;
        }
        Field[] fields = o.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(o);
                        if (!keepNulls && value == null) {
                            return;
                        }
                        result.put(field.getName(), (V) value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return result;
    }

    /**
     * 拷贝一个对象的字段值到另一个对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param cover  目标对象字段值不为空时是否覆盖
     */
    public static void copyBean(Object source, Object target, boolean cover) {
        if (source == null || target == null) {
            return;
        }
        Map<String, Field> sourceFieldMap = aliasFieldMap(source.getClass());
        Map<String, Field> targetFieldMap = aliasFieldMap(target.getClass());
        sourceFieldMap.forEach((key, value) -> {
            try {
                //获取目标对象的同名字段
                Field field = targetFieldMap.get(key);
                if (field == null) {
                    return;
                }
                field.setAccessible(true);
                value.setAccessible(true);
                //允许字段值覆盖时，则直接给目标对象的字段设置对于值
                if (cover) {
                    field.set(target, value.get(source));
                    return;
                }
                //若不允许覆盖则，先判断目标对象该字段的值是否为空，若为空则设置新值，若不为空则不设置
                Object o = field.get(target);
                if (o != null) {
                    return;
                }
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 拷贝一个对象的字段值到另一个对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static <T> T copyBean(Object source, T target) {
        copyBean(source, target, true);
        return target;
    }

    /**
     * 创建一个指定类型的目标对象，并将源对象中属性值赋值给目标对象的对应字段
     *
     * @param source      源对象
     * @param targetClass 目标对象的类型
     * @param <T>         目标泛型
     * @return 目标对象
     * @throws Exception
     */
    public static <T> T copyBean(Object source, Class<T> targetClass) throws Exception {
        T t = targetClass.newInstance();
        copyBean(source, t, true);
        return t;
    }

    /**
     * 对敏感字段脱敏
     *
     * @param o
     */
    public static void desensitizationData(Object o) {
        if (o == null) {
            return;
        }
        Arrays.stream(o.getClass().getDeclaredFields())
                .map(field -> Tuple2.of(field, field.getAnnotation(Sensitive.class)))
                .filter(tuple2 -> tuple2._2 != null)
                .forEach(tuple2 -> {
                    try {
                        //获取注解信息，从注解中获取替代接口
                        Sensitive sensitive = tuple2._2;
                        Class<? extends Sensitive.Replacer> value = sensitive.value();
                        Sensitive.Replacer replacer = value.newInstance();
                        Field field = tuple2._1;
                        field.setAccessible(true);
                        Object sourceValue = field.get(o);
                        Object targetValue = replacer.replace(sourceValue);
                        field.set(o, targetValue);
                    } catch (Exception e) {
                        throw new RuntimeException("Desensitization data error", e);
                    }
                });
    }

    /**
     * 根据Class获取该类的包含别名的字段信息
     *
     * @param tClass 指定类型
     * @return
     */
    private static Map<String, Field> aliasFieldMap(Class tClass) {
        //获取该类中多有字段名以及别名和字段的对应信息
        List<Tuple2<String, Field>> allFiledList = Arrays.stream(tClass.getDeclaredFields())
                .flatMap(field -> {
                    List<Tuple2<String, Field>> fields = new ArrayList<>();
                    fields.add(Tuple2.of(field.getName(), field));
                    //获取别名注解，若包含别名注解且有添加别名，则会别名和字段信息添加到字段列表中
                    Alias annotation = field.getAnnotation(Alias.class);
                    if (annotation == null) {
                        return fields.stream();
                    }
                    String[] aliasNames = annotation.value();
                    if (aliasNames == null || aliasNames.length == 0) {
                        return fields.stream();
                    }
                    for (String aliasName : aliasNames) {
                        if (StringUtils.isBlank(aliasName) || StringUtils.equals(field.getName(), aliasName)) {
                            continue;
                        }
                        fields.add(Tuple2.of(aliasName, field));
                    }
                    return fields.stream();
                }).collect(Collectors.toList());

        /**
         * 校验是否存储有歧义的别名，是否别名与其他字段的字段名重复
         */
        Set<String> fieldNameSet = new HashSet<>();
        allFiledList.forEach(tuple2 -> {
            if (fieldNameSet.contains(tuple2._1)) {
                throw new RuntimeException("Field alias[" + tuple2._1 + "] is ambiguous");
            }
            fieldNameSet.add(tuple2._1);
        });
        return allFiledList.stream().collect(Collectors.toMap(tuple2 -> tuple2._1, tuple2 -> tuple2._2));
    }
}
