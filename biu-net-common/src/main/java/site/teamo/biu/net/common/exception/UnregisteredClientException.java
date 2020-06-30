package site.teamo.biu.net.common.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */

/**
 * 未注册的客户端异常
 */
public class UnregisteredClientException extends Exception{
    public UnregisteredClientException() {
        super();
    }

    public UnregisteredClientException(String message) {
        super(message);
    }

    public UnregisteredClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnregisteredClientException(Throwable cause) {
        super(cause);
    }

    protected UnregisteredClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
