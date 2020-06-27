package site.teamo.biu.net.common.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public class ProtocolInconsistencyException extends Exception {
    public ProtocolInconsistencyException() {
        super();
    }

    public ProtocolInconsistencyException(String message) {
        super(message);
    }

    public ProtocolInconsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolInconsistencyException(Throwable cause) {
        super(cause);
    }

    protected ProtocolInconsistencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
