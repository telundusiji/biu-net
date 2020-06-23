package site.teamo.biu.net.common.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
public class IllegalInformationException extends Exception {
    public IllegalInformationException() {
        super();
    }

    public IllegalInformationException(String message) {
        super(message);
    }

    public IllegalInformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInformationException(Throwable cause) {
        super(cause);
    }

    protected IllegalInformationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
