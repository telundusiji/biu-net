package site.teamo.biu.net.common.message;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
public enum MessageType {
    UN_KNOWN("未知数据", -1, Object.class),
    PING("心跳数据", 0, Ping.Data.class),
    LOGIN_REQUEST("客户端登陆请求", 1001, Login.Request.class),
    LOGIN_RESPONSE("登陆响应信息", 1002, Login.Response.class),
    PACKAGE_DATA_REQUEST("转发数据", 2001, PackageData.Request.class),
    PACKAGE_DATA_RESPONSE("转发响应数据", 2002, PackageData.Response.class);
    public final String name;
    public final int type;
    public final Class<?> tClass;

    public <T> BiuNetMessage<T> getMessage(T content) {
        return new BiuNetMessage<T>()
                .setType(this)
                .setContent(content);
    }

    MessageType(String name, int type, Class<?> tClass) {
        this.name = name;
        this.type = type;
        this.tClass = tClass;
    }
}
