package site.teamo.biu.net.common.message;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/18
 */
public interface CloseProxyClient {
    class Data {
        public final String proxyCtxId;

        public Data(String proxyCtxId) {
            this.proxyCtxId = proxyCtxId;
        }

        public static BiuNetMessage<CloseProxyClient.Data> buildData(String proxyCtxId) {
            return new BiuNetMessage<CloseProxyClient.Data>()
                    .setType(MessageType.CLOSE_PROXY_CLIENT)
                    .setContent(new CloseProxyClient.Data(proxyCtxId));
        }
    }
}
