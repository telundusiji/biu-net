package site.teamo.biu.net.common.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/20
 */

/**
 * 参数错误异常
 */
public class BiuNetRuntimeException extends RuntimeException {

    private ResponseCode errorCode;

    public BiuNetRuntimeException(ResponseCode errorCode) {
        super(errorCode.description());
        this.errorCode = errorCode;
    }

    public BiuNetRuntimeException(ResponseCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BiuNetRuntimeException(ResponseCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BiuNetRuntimeException(ResponseCode errorCode, Throwable cause) {
        super(errorCode.description(), cause);
        this.errorCode = errorCode;
    }

    protected BiuNetRuntimeException(ResponseCode errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ResponseCode getErrorCode() {
        return errorCode;
    }
}
