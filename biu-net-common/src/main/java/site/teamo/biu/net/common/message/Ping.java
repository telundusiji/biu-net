package site.teamo.biu.net.common.message;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public interface Ping {
    class Data {
        public final String host;

        public Data(String host) {
            this.host = host;
        }
    }
}
