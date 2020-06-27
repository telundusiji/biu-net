package site.teamo.biu.net.common.enums;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public enum BiuNetMessageHead {
    PING_REQUEST("心跳数据", 0),
    PING_RESPONSE("心跳返回数据", 1),
    LOGIN_REQUEST("客户端登陆请求", 1001),
    LOGIN_RESPONSE("登陆响应信息", 1002),
    FORWARD_REQUEST("代理服务器向客户端的请求", 2001),
    FORWARD_RESPONSE("客户端向服务端响应数据", 2002);
    public final String name;
    public final int type;

    BiuNetMessageHead(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public static BiuNetMessageHead valueOfByType(int type) {
        BiuNetMessageHead[] values = values();
        for (BiuNetMessageHead value : values) {
            if (value.type == type) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant for type "+type);
    }
}
