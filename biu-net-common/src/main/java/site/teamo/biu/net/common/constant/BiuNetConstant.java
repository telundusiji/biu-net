package site.teamo.biu.net.common.constant;

import io.netty.util.AttributeKey;
import site.teamo.biu.net.common.bean.ProxyClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetConstant {
    private BiuNetConstant(){}

    public static final AttributeKey<String> clientKey = AttributeKey.valueOf("clientKey");

    public static final AttributeKey<ProxyServerInfo> proxyServerInfo = AttributeKey.valueOf("proxyServerInfo");

    public static final AttributeKey<ProxyClientInfo> proxyClientInfo = AttributeKey.valueOf("proxyClientInfo");

    public static final int register_head = 1001;

    public static final int data_head = 2001;
}
