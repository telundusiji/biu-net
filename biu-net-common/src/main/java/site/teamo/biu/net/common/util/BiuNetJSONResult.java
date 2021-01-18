package site.teamo.biu.net.common.util;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@Builder
@NoArgsConstructor
public class BiuNetJSONResult {
    // 响应业务状态
    private Integer code;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;


    public static BiuNetJSONResult build(Integer status, String msg, Object data) {
        return new BiuNetJSONResult(status, msg, data);
    }

    public static BiuNetJSONResult ok(Object data) {
        return new BiuNetJSONResult(data);
    }

    public static BiuNetJSONResult ok() {
        return new BiuNetJSONResult(null);
    }

    public static BiuNetJSONResult errorMsg(String msg) {
        return new BiuNetJSONResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static BiuNetJSONResult errorMap(Object data) {
        return new BiuNetJSONResult(HttpStatus.NOT_IMPLEMENTED.value(), "error", data);
    }

    public static BiuNetJSONResult errorTokenMsg(String msg) {
        return new BiuNetJSONResult(HttpStatus.BAD_GATEWAY.value(), msg, null);
    }

    public static BiuNetJSONResult errorException(String msg) {
        return new BiuNetJSONResult(555, msg, null);
    }

    public static BiuNetJSONResult errorUserQQ(String msg) {
        return new BiuNetJSONResult(556, msg, null);
    }

    public BiuNetJSONResult(Integer status, String msg, Object data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    public BiuNetJSONResult(Object data) {
        this.code = HttpStatus.OK.value();
        this.msg = "OK";
        this.data = data;
    }

}
