package site.teamo.biu.net.common.util;

import java.net.InetAddress;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/18
 */
public class NetUtil {
    public static String myHost() {
        String myHost = "localhost";
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostName();
        } catch (Exception e) {
            return myHost;
        }
    }
}
