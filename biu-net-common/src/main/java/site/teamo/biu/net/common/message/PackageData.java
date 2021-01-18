package site.teamo.biu.net.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/14
 */
public interface PackageData {
    @Data
    @Accessors(chain = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request {
        private int proxyServerPort;
        private String proxyCtxId;
        private String targetHost;
        private int targetPort;
        private byte[] data;

        public BiuNetMessage<Request> buildData() {
            return new BiuNetMessage<Request>()
                    .setType(MessageType.PACKAGE_DATA_REQUEST)
                    .setContent(this);
        }
    }

    @Data
    @Accessors(chain = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Response {
        private int proxyServerPort;
        private String proxyCtxId;
        private byte[] data;

        public BiuNetMessage<Response> buildData() {
            return new BiuNetMessage<Response>()
                    .setType(MessageType.PACKAGE_DATA_RESPONSE)
                    .setContent(this);
        }
    }
}
