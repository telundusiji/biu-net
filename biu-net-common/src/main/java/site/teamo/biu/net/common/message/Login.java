package site.teamo.biu.net.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
public interface Login {

    @Data
    @Accessors(chain = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request {
        private String id;
        private String name;
        private String password;
    }

    @Data
    @Accessors(chain = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Response {
        private LoginResult result;
        private String msg;

        public static BiuNetMessage<Response> failed(String msg) {
            return new BiuNetMessage<Response>()
                    .setType(MessageType.LOGIN_RESPONSE)
                    .setContent(Login.Response.builder()
                            .result(LoginResult.FAILED)
                            .msg(msg)
                            .build());
        }

        public static BiuNetMessage<Response> success() {
            return new BiuNetMessage<Response>()
                    .setType(MessageType.LOGIN_RESPONSE)
                    .setContent(Login.Response.builder()
                            .result(LoginResult.SUCCESS)
                            .build());
        }
    }

    enum LoginResult {
        SUCCESS,
        FAILED;
    }
}
