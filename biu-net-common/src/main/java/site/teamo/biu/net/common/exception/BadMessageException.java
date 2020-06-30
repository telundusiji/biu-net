package site.teamo.biu.net.common.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */

/**
 * 损坏的数据异常
 */
public class BadMessageException extends Exception {
    public BadMessageException() {
        super();
    }

    public BadMessageException(String message) {
        super(message);
    }

    public BadMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadMessageException(Throwable cause) {
        super(cause);
    }

    protected BadMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
