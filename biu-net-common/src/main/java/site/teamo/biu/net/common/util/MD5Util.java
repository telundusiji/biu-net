package site.teamo.biu.net.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public class MD5Util {

    public static String toMD5(String strValue) throws NoSuchAlgorithmException {
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest(strValue.getBytes()));
    }

}
