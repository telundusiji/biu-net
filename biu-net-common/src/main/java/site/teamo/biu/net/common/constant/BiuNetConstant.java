package site.teamo.biu.net.common.constant;

import io.netty.util.AttributeKey;
import site.teamo.biu.net.common.bean.ClientPassport;
import site.teamo.biu.net.common.bean.ProxyClientInfo;
import site.teamo.biu.net.common.bean.ProxyServerInfo;
import site.teamo.biu.net.common.message.LoginRequest;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
public class BiuNetConstant {
    private BiuNetConstant(){}

    public static final AttributeKey<LoginRequest.SimpleProtocol> LOGIN_REQUEST_PROTOCOL = AttributeKey.valueOf("login.protocol");

    public static final AttributeKey<ProxyServerInfo> proxyServerInfo = AttributeKey.valueOf("proxyServerInfo");

    public static final AttributeKey<ProxyClientInfo> proxyClientInfo = AttributeKey.valueOf("proxyClientInfo");

    public static final int login_head = 1001;

    public static final int data_head = 2001;
}
