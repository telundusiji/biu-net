package site.teamo.biu.net.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/23
 */
public class IDFactory {

    private static final FastDateFormat format = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

    public static String shortId() {
        return format.format(new Date()) + RandomStringUtils.random(2, "abcdefghijklmnopqrstuvwxy");
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
