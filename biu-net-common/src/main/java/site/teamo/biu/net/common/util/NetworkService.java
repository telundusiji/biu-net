package site.teamo.biu.net.common.util;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/16
 */
public interface NetworkService {

    void start(boolean permanent);

    void shutdown();

    Status status();

    default boolean isStarted() {
        synchronized (this) {
            return Status.STARTED.equals(status());
        }
    }

    enum Status {
        STOPPED,
        STARTED;
    }
}
